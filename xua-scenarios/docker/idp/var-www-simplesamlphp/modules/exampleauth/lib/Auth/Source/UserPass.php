<?php

include 'SubjectConfirmationDataWithType.php';

// this is a modified version of the UserPass class found in SimpleSAMLPhp, to hook in our extra attributes
class sspmod_exampleauth_Auth_Source_UserPass extends sspmod_core_Auth_UserPassBase {
	private $users;

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

	protected function login($authStateId, $username, $password) {
		assert('is_string($username)');
		assert('is_string($password)');

		SimpleSAML\Logger::info('User: ' . $username);

		$userpass = $username . ':' . $password;
		if (!array_key_exists($userpass, $this->users)) {
			throw new SimpleSAML_Error_Error('WRONGUSERPASS');
		}

		/* Here we retrieve the state array we saved in the authenticate-function. */
		SimpleSAML\Logger::info('StageID: '.self::STAGEID);
		$state = SimpleSAML_Auth_State::loadState($authStateId, self::STAGEID);

		$state['saml:NameIDFormat'] = 'urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName';

		// generate bootstrap token
		$assertion = self::getBootstrapToken($username, $state);
		$element = $assertion->toXML();
		$xml = new DOMDocument();
		$xml->preserveWhiteSpace = false; 
		$xml->formatOutput = false; 
		$cloned = $element->cloneNode(TRUE);
		$xml->appendChild($xml->importNode($cloned,TRUE));
		$xmlString = $xml->saveXML();

		// generate UserAuthorizationList (DOMNodeList with DOMElement children is needed)
		$tmpDoc = new DOMDocument();
		$tmpDoc->loadXML(self::getUserAuthorizationExample());
		$child = $tmpDoc->documentElement;
		$tmpDoc2 = new DOMDocument();
		$importedChild = $tmpDoc2->importNode($child, true);
		$tmpElement = $tmpDoc2->createElementNS("urn:oasis:names:tc:SAML:2.0:assertion", "AttributeValue");

		$tmpElement->appendChild($importedChild);
		$nodelist = $tmpElement->childNodes;

		$attributes = array(
			'uid' => array($username),
			'urn:liberty:disco:2006-08:DiscoveryEPR' => base64_encode($xmlString),
			'dk:gov:saml:attribute:AssuranceLevel' => '3',
			'dk:healthcare:saml:attribute:UserAuthorizations' => $nodelist
        	);

		return self::normalizeAttributesArray($attributes);
	}

	    private function normalizeAttributesArray($attributes)
	    {
		if (!is_array($attributes)) {
		    throw new \InvalidArgumentException(
		        'The attributes array is not an array, it is: '.print_r($attributes, true).'".'
		    );
		}

		$newAttrs = array();
		foreach ($attributes as $name => $values) {
		    if (!is_string($name)) {
		        throw new \InvalidArgumentException('Invalid attribute name: "'.print_r($name, true).'".');
		    }

		    $values = SimpleSAML\Utils\Arrays::arrayize($values);
		    $newAttrs[$name] = $values;
		}

		return $newAttrs;
	    }


	private function getUserAuthorizationExample() {
                return '<uap:UserAuthorizationList xmlns:uap="urn:dk:healthcare:saml:user_authorization_profile:1.0"><uap:UserAuthorization><uap:AuthorizationCode>341KY</uap:AuthorizationCode><uap:EducationCode>7170</uap:EducationCode><uap:EducationType>Læge</uap:EducationType></uap:UserAuthorization></uap:UserAuthorizationList>';
	}


	private function getBootstrapToken($username,$state) {
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

	private function addAttributeToAssertion(&$attributes, &$attributesValueTypes, $name, $value, $valueType){
		$attributes[$name] = array();
		$attributesValueTypes[$name] = array();
		$attributes[$name][] = $value;
		$attributesValueTypes[$name][] = $valueType;
	}

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
