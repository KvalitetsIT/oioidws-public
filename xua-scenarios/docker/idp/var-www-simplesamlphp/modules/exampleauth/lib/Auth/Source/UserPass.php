<?php

include 'SubjectConfirmationDataWithType.php';

/**
 * Example authentication source - username & password.
 *
 * This class is an example authentication source which stores all username/passwords in an array,
 * and authenticates users against this array.
 *
 * @author Olav Morken, UNINETT AS.
 * @package SimpleSAMLphp
 */
class sspmod_exampleauth_Auth_Source_UserPass extends sspmod_core_Auth_UserPassBase {


	/**
	 * Our users, stored in an associative array. The key of the array is "<username>:<password>",
	 * while the value of each element is a new array with the attributes for each user.
	 */
	private $users;


	/**
	 * Constructor for this authentication source.
	 *
	 * @param array $info  Information about this authentication source.
	 * @param array $config  Configuration.
	 */
	public function __construct($info, $config) {
		assert('is_array($info)');
		assert('is_array($config)');
		SimpleSAML\Logger::info('Info: ' . print_r($info,TRUE));
		SimpleSAML\Logger::info('Config: ' . print_r($config,TRUE));

		// Call the parent constructor first, as required by the interface
		parent::__construct($info, $config);

		$this->users = array();

		// Validate and parse our configuration
		foreach ($config as $userpass => $attributes) {
			if (!is_string($userpass)) {
				throw new Exception('Invalid <username>:<password> for authentication source ' .
					$this->authId . ': ' . $userpass);
			}

			$userpass = explode(':', $userpass, 2);
			if (count($userpass) !== 2) {
				throw new Exception('Invalid <username>:<password> for authentication source ' .
					$this->authId . ': ' . $userpass[0]);
			}
			$username = $userpass[0];
			$password = $userpass[1];

			try {
				$attributes = SimpleSAML\Utils\Attributes::normalizeAttributesArray($attributes);
			} catch(Exception $e) {
				throw new Exception('Invalid attributes for user ' . $username .
					' in authentication source ' . $this->authId . ': ' .
					$e->getMessage());
			}

			$this->users[$username . ':' . $password] = $attributes;
		}
	}


	/**
	 * Attempt to log in using the given username and password.
	 *
	 * On a successful login, this function should return the users attributes. On failure,
	 * it should throw an exception. If the error was caused by the user entering the wrong
	 * username or password, a SimpleSAML_Error_Error('WRONGUSERPASS') should be thrown.
	 *
	 * Note that both the username and the password are UTF-8 encoded.
	 *
	 * @param string $username  The username the user wrote.
	 * @param string $password  The password the user wrote.
	 * @return array  Associative array with the users attributes.
	 */
	protected function login($authStateId, $username, $password) {
		assert('is_string($username)');
		assert('is_string($password)');
		SimpleSAML\Logger::info('User: ' . $username);

		$userpass = $username . ':' . $password;
		if (!array_key_exists($userpass, $this->users)) {
			throw new SimpleSAML_Error_Error('WRONGUSERPASS');
		}

		/* From here code is modified by us */

		/* Here we retrieve the state array we saved in the authenticate-function. */
		SimpleSAML\Logger::info('StageID: '.self::STAGEID);
		$state = SimpleSAML_Auth_State::loadState($authStateId, self::STAGEID);
		SimpleSAML\Logger::info('Hello after');
		//SimpleSAML\Logger::info('State:'.print_r($state,TRUE));

		$state['saml:NameIDFormat'] = 'urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName';

		$assertion = self::getBootstrapToken($username, $state);
		$element = $assertion->toXML();
		$xml = new DOMDocument();
		$xml->preserveWhiteSpace = false; 
		$xml->formatOutput = false; 
		$cloned = $element->cloneNode(TRUE);
		$xml->appendChild($xml->importNode($cloned,TRUE));
		/** @var mixed $xmlString */
		$xmlString = $xml->saveXML();
		SimpleSAML\Logger::info($xmlString);

		/** @var mixed $attributes */
		$attributes = array(
			'uid' => array($username),
			'urn:liberty:disco:2006-08:DiscoveryEPR' => base64_encode($xmlString),
			'dk:gov:saml:attribute:AssuranceLevel' => '3'
        	);

		return SimpleSAML\Utils\Attributes::normalizeAttributesArray($attributes);
		/* Some commented stuff */
		/** $attributes = $this->users[$userpass];
		$attributes['urn:liberty:disco:2006-08:DiscoveryEPR'] = base64_encode($xmlString);
		$attributes = SimpleSAML\Utils\Attributes::normalizeAttributesArray($attributes);
		return $attributes; */
	}

	/** 
		Method that builds custom assertion.
		@author psu@digital-identity.dk
	*/
	private function getBootstrapToken($username,$state){
		if (!array_key_exists('SPMetadata',$state)) {
			throw new SimpleSAML_Error_Error('METADATANOTFOUND');
		}

		$spMetadataArray = $state["SPMetadata"];
        	$spMetadata = SimpleSAML_Configuration::loadFromArray($spMetadataArray);

		$assertion = new SAML2\Assertion();
		$assertion->setIssuer('http://idp.sundhedsdatastyrelsen.dk');
		$assertion->setNameID(array("Format" => "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName", "Value" => $username));

		$subjectConfirmation = new SAML2\XML\saml\SubjectConfirmation();
		$subjectConfirmation->Method="urn:oasis:names:tc:SAML:2.0:cm:holder-of-key";
		$subjectConfirmation->NameID = SAML2\XML\saml\NameID::fromArray(array("Format" => "urn:oasis:tc:SAML2:2.0:nameid-format:entity", "Value" => $spMetadata->getString('entityid', null)));

		/** @var string $X509Certificate */
		$X509Certificate = '';

		/** @var array $keys */
		$keys = $state['SPMetadata']['keys'];
		if($keys != null){
			/** @var mixed $index */
			$index = null;
			SimpleSAML\Logger::info('Keys found');
			foreach ($keys as $key => $val) {
				SimpleSAML\Logger::info('Key: '.$key);
				SimpleSAML\Logger::info('Val: '.$val['signing']);
				if ($val['signing'] == 1) {
					$index = $key;
				}
			}
			if($index !== null){
				$X509Certificate = $keys[$index]['X509Certificate'];
			}
		}

		$subjectConfirmationDataString = '
			<SubjectConfirmationData>
				<KeyInfo xmlns="http://www.w3.org/2000/09/xmldsig#">
				<X509Data>
					<X509Certificate>'.$X509Certificate.'</X509Certificate>
				</X509Data>
				</KeyInfo>
			</SubjectConfirmationData>
		';

		$d = new DOMDocument();
		$d->loadXML($subjectConfirmationDataString);

		$subjectConfirmationDataXML = $d->getElementsByTagName("SubjectConfirmationData")->item(0);
		$d = null;

		$subjectConfirmationData = new SubjectConfirmationDataWithType($subjectConfirmationDataXML);

		$subjectConfirmation->SubjectConfirmationData=$subjectConfirmationData;

		$assertion->setSubjectConfirmation([$subjectConfirmation]); // has to be array

		$notOnOrAfter = new DateTime();//2018-03-18T16:35:58.747Z
		SimpleSAML\Logger::info('NewDate: '.$notOnOrAfter->format('Y-m-d\TH:i:s.u\Z'));
		$notOnOrAfter->add(new DateInterval('PT1H'));
		SimpleSAML\Logger::info('AfterDate: '.$notOnOrAfter->format('Y-m-d\TH:i:s.u\Z'));
		$assertion->setNotOnOrAfter(SAML2\Utils::xsDateTimeToTimestamp($notOnOrAfter->format('Y-m-d\TH:i:s.u\Z'))); //add 1 hour
		$assertion->setValidAudiences(['http://sts.sundhedsdatastyrelsen.dk/']);
		
		//Attributes
		$attributes = array();
		$attributesValueTypes = array();
		
		$assertion->setAttributeNameFormat('urn:oasis:names:tc:SAML:2.0:attrname-format:basic');

		self::addAttributeToAssertion($attributes, $attributesValueTypes, 'dk:gov:saml:attribute:AssuranceLevel', '3', null);

		$assertion->setAttributes($attributes);
		$assertion->setAttributesValueTypes($attributesValueTypes);

   		//Sign
        	$idp = SimpleSAML_IdP::getByState($state);
        	$idpMetadata = $idp->getConfig();
        
        	self::addSign($idpMetadata, $spMetadata, $assertion);
		
		return $assertion;
	}
	
	/** 
		Helper method. Because of the way attributes and attributeValueTypes are stored this method
		is helpful managing code.

		@author psu@digital-identity.dk
	*/
	private function addAttributeToAssertion(&$attributes, &$attributesValueTypes, $name, $value, $valueType){
		$attributes[$name] = array();
		$attributesValueTypes[$name] = array();
		$attributes[$name][] = $value;
		$attributesValueTypes[$name][] = $valueType;
			
	}
	
	/**
	* Add signature key and sender certificate to an element (Message or Assertion).
	*
	* @param SimpleSAML_Configuration $srcMetadata The metadata of the sender.
	* @param SimpleSAML_Configuration $dstMetadata The metadata of the recipient.
	* @param \SAML2\SignedElement $element The element we should add the data to.
	* 
	* Moved here by psu@digital-identity.dk
	*/
	private function addSign(SimpleSAML_Configuration $srcMetadata, SimpleSAML_Configuration $dstMetadata, \SAML2\SignedElement $element) 
	{
		$dstPrivateKey = $dstMetadata->getString('signature.privatekey', null);

		if ($dstPrivateKey !== null) {
		    $keyArray = SimpleSAML\Utils\Crypto::loadPrivateKey($dstMetadata, true, 'signature.');
		    $certArray = SimpleSAML\Utils\Crypto::loadPublicKey($dstMetadata, false, 'signature.');
		} else {
		    $keyArray = SimpleSAML\Utils\Crypto::loadPrivateKey($srcMetadata, true);
		    $certArray = SimpleSAML\Utils\Crypto::loadPublicKey($srcMetadata, false);
		}

		$algo = XMLSecurityKey::RSA_SHA256;

		$privateKey = new XMLSecurityKey($algo, array('type' => 'private'));
		if (array_key_exists('password', $keyArray)) {
		    $privateKey->passphrase = $keyArray['password'];
		}
		$privateKey->loadKey($keyArray['PEM'], false);

		$element->setSignatureKey($privateKey);

		if ($certArray === null) {
		    // we don't have a certificate to add
		    return;
		}

		if (!array_key_exists('PEM', $certArray)) {
		    // we have a public key with only a fingerprint
		    return;
		}

		$element->setCertificates(array($certArray['PEM']));
	}
}
