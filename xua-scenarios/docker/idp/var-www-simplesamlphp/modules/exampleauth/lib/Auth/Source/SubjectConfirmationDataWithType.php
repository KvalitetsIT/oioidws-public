<?php

use RobRichards\XMLSecLibs\XMLSecurityDSig;
use SAML2\Constants;
use SAML2\Utils;
use SAML2\XML\Chunk;
use SAML2\XML\ds\KeyInfo;

/**
	Added by Digital-Identity.dk
*/

class SubjectConfirmationDataWithType extends SAML2\XML\saml\SubjectConfirmationData
{

    public function toXML(\DOMElement $parent)
    {
	$e = parent::toXML($parent);
	$e->setAttributeNS(\SAML2\Constants::NS_XSI, 'xsi:type', 'saml:KeyInfoConfirmationDataType');

        return $e;
    }

}
