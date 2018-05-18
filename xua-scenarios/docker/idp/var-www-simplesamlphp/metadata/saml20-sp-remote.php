<?php

$metadata['https://saml.wsc-idws-xua'] = array (
  'entityid' => 'https://saml.wsc-idws-xua',
  'description' => 
  array (
  ),
  'OrganizationName' => 
  array (
    'en' => 'cxf-sample',
  ),
  'name' => 
  array (
    'en' => 'https://saml.wsc-idws-xua',
  ),
  'OrganizationDisplayName' => 
  array (
    'en' => 'cxf-sample',
  ),
  'url' => 
  array (
    'en' => 'http://digst.dk',
  ),
  'OrganizationURL' => 
  array (
    'en' => 'http://digst.dk',
  ),
  'contacts' => 
  array (
    0 => 
    array (
      'contactType' => 'technical',
      'company' => 'cxf-sample',
      'emailAddress' => 
      array (
        0 => 'bsg@digital-identity.dk',
      ),
    ),
  ),
  'metadata-set' => 'saml20-sp-remote',
  'AssertionConsumerService' => 
  array (
    0 => 
    array (
      'Binding' => 'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST',
      'Location' => 'https://wsc-idws-xua:8095/cxf-sp-ws-consumer/saml/SAMLAssertionConsumer',
      'index' => 0,
      'isDefault' => true,
    ),
  ),
  'SingleLogoutService' => 
  array (
    0 => 
    array (
      'Binding' => 'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect',
      'Location' => 'https://wsc-idws-xua:8095/cxf-sp-ws-consumer/saml/LogoutServiceHTTPRedirect',
      'ResponseLocation' => 'https://wsc-idws-xua:8095/cxf-sp-ws-consumer/saml/LogoutServiceHTTPRedirectResponse',
    ),
    1 => 
    array (
      'Binding' => 'urn:oasis:names:tc:SAML:2.0:bindings:SOAP',
      'Location' => 'https://wsc-idws-xua:8095/cxf-sp-ws-consumer/saml/LogoutServiceSOAP',
    ),
    2 => 
    array (
      'Binding' => 'urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST',
      'Location' => 'https://wsc-idws-xua:8095/cxf-sp-ws-consumer/saml/LogoutServiceHTTPPost',
      'ResponseLocation' => 'https://wsc-idws-xua:8095/cxf-sp-ws-consumer/saml/LogoutServiceHTTPRedirectResponse',
    ),
  ),
  'NameIDFormat' => 'urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName',
  'keys' => 
  array (
    0 => 
    array (
      'encryption' => false,
      'signing' => true,
      'type' => 'X509Certificate',
      'X509Certificate' => 'MIIGJjCCBQ6gAwIBAgIEWRvaNzANBgkqhkiG9w0BAQsFADBIMQswCQYDVQQGEwJE
SzESMBAGA1UECgwJVFJVU1QyNDA4MSUwIwYDVQQDDBxUUlVTVDI0MDggU3lzdGVt
dGVzdCBYWElJIENBMB4XDTE4MDQxMDA1NTY0MVoXDTIxMDQxMDA1NTU0MVowgZEx
CzAJBgNVBAYTAkRLMS4wLAYDVQQKDCVTdW5kaGVkc2RhdGFzdHlyZWxzZW4gLy8g
Q1ZSOjMzMjU3ODcyMVIwIAYDVQQFExlDVlI6MzMyNTc4NzItRklEOjIyOTMzOTIy
MC4GA1UEAwwnSURXUyBYVUEgVGVzdCBXU0MgKGZ1bmt0aW9uc2NlcnRpZmlrYXQp
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk4a61B9rD5WFm0e3B++g
xN7YbOx/ymw++FBPF2GxdEqe0mj4eIEwMKJ4jA5JJt5WULSLdGKjvJZK/Gbq98UH
vWwTvDPtF6+Y1gypItKwghpVgJJxWwvM6fxcYD0zN+MHilSk4ZNbFK14rJhNhvsh
nfmO1VejVTKJzmkY2kSGAKw9adVWpZSVbJg66tT0z3ZHyGeRsSAQ79pl3Jlcfg+I
Z12vx5rBkAtHEPIzvrZRzLHOeMkR7+jqwbnpmgpLGtdSY94Elm4ol9MQbFnqUGw3
n9Rvvk7Ar3DA1+E71cEhhFwA/sPwyojqz7VYAR8qZF83hFwe/Ne2DL0aV96Ko/XL
XwIDAQABo4ICzDCCAsgwDgYDVR0PAQH/BAQDAgO4MIGXBggrBgEFBQcBAQSBijCB
hzA8BggrBgEFBQcwAYYwaHR0cDovL29jc3Auc3lzdGVtdGVzdDIyLnRydXN0MjQw
OC5jb20vcmVzcG9uZGVyMEcGCCsGAQUFBzAChjtodHRwOi8vZi5haWEuc3lzdGVt
dGVzdDIyLnRydXN0MjQwOC5jb20vc3lzdGVtdGVzdDIyLWNhLmNlcjCCASAGA1Ud
IASCARcwggETMIIBDwYNKwYBBAGB9FECBAYEAjCB/TAvBggrBgEFBQcCARYjaHR0
cDovL3d3dy50cnVzdDI0MDguY29tL3JlcG9zaXRvcnkwgckGCCsGAQUFBwICMIG8
MAwWBURhbklEMAMCAQEagatEYW5JRCB0ZXN0IGNlcnRpZmlrYXRlciBmcmEgZGVu
bmUgQ0EgdWRzdGVkZXMgdW5kZXIgT0lEIDEuMy42LjEuNC4xLjMxMzEzLjIuNC42
LjQuMi4gRGFuSUQgdGVzdCBjZXJ0aWZpY2F0ZXMgZnJvbSB0aGlzIENBIGFyZSBp
c3N1ZWQgdW5kZXIgT0lEIDEuMy42LjEuNC4xLjMxMzEzLjIuNC42LjQuMi4wgawG
A1UdHwSBpDCBoTA9oDugOYY3aHR0cDovL2NybC5zeXN0ZW10ZXN0MjIudHJ1c3Qy
NDA4LmNvbS9zeXN0ZW10ZXN0MjIxLmNybDBgoF6gXKRaMFgxCzAJBgNVBAYTAkRL
MRIwEAYDVQQKDAlUUlVTVDI0MDgxJTAjBgNVBAMMHFRSVVNUMjQwOCBTeXN0ZW10
ZXN0IFhYSUkgQ0ExDjAMBgNVBAMMBUNSTDM2MB8GA1UdIwQYMBaAFKuoAUQZsLND
mdr6fMzSABgD5zy/MB0GA1UdDgQWBBSNMd+ipv+Sfh/O01S7ntEj5AX5BjAJBgNV
HRMEAjAAMA0GCSqGSIb3DQEBCwUAA4IBAQAKAXMYlk4jVxa6bWDej4urxHcSZmUa
NG7uojcjBe81a3Lx0OMdUoQPojeatlRyQmrQU8l1JXUb/33utXn8GJZoJ7iMjmaX
S7Eie2pXKR0EbrIFW6MzB5fx3Yk7IQzkXgPAtCCuqLXF+cwRFwZaBBQ/spPx0Pg/
0gYxFWEklC99jmiiaCYF4T4N1x3L2fF0IfKJRY4aGGKuGY3xZwj2lGmrrcv1Nnih
l9BiCcK8wfufx4RxkGYn2RfUy6vP0FO3ZM164xfJW4iOI7ETS3EQyoZUVBhqeRzX
NXJz+wme3kb6HYR7bU3AzacyRIcWPAYQfZ9+WckLNklGp+vdn/ZwNjPo',
    ),
    1 => 
    array (
      'encryption' => true,
      'signing' => false,
      'type' => 'X509Certificate',
      'X509Certificate' => 'MIIGJjCCBQ6gAwIBAgIEWRvaNzANBgkqhkiG9w0BAQsFADBIMQswCQYDVQQGEwJE
SzESMBAGA1UECgwJVFJVU1QyNDA4MSUwIwYDVQQDDBxUUlVTVDI0MDggU3lzdGVt
dGVzdCBYWElJIENBMB4XDTE4MDQxMDA1NTY0MVoXDTIxMDQxMDA1NTU0MVowgZEx
CzAJBgNVBAYTAkRLMS4wLAYDVQQKDCVTdW5kaGVkc2RhdGFzdHlyZWxzZW4gLy8g
Q1ZSOjMzMjU3ODcyMVIwIAYDVQQFExlDVlI6MzMyNTc4NzItRklEOjIyOTMzOTIy
MC4GA1UEAwwnSURXUyBYVUEgVGVzdCBXU0MgKGZ1bmt0aW9uc2NlcnRpZmlrYXQp
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk4a61B9rD5WFm0e3B++g
xN7YbOx/ymw++FBPF2GxdEqe0mj4eIEwMKJ4jA5JJt5WULSLdGKjvJZK/Gbq98UH
vWwTvDPtF6+Y1gypItKwghpVgJJxWwvM6fxcYD0zN+MHilSk4ZNbFK14rJhNhvsh
nfmO1VejVTKJzmkY2kSGAKw9adVWpZSVbJg66tT0z3ZHyGeRsSAQ79pl3Jlcfg+I
Z12vx5rBkAtHEPIzvrZRzLHOeMkR7+jqwbnpmgpLGtdSY94Elm4ol9MQbFnqUGw3
n9Rvvk7Ar3DA1+E71cEhhFwA/sPwyojqz7VYAR8qZF83hFwe/Ne2DL0aV96Ko/XL
XwIDAQABo4ICzDCCAsgwDgYDVR0PAQH/BAQDAgO4MIGXBggrBgEFBQcBAQSBijCB
hzA8BggrBgEFBQcwAYYwaHR0cDovL29jc3Auc3lzdGVtdGVzdDIyLnRydXN0MjQw
OC5jb20vcmVzcG9uZGVyMEcGCCsGAQUFBzAChjtodHRwOi8vZi5haWEuc3lzdGVt
dGVzdDIyLnRydXN0MjQwOC5jb20vc3lzdGVtdGVzdDIyLWNhLmNlcjCCASAGA1Ud
IASCARcwggETMIIBDwYNKwYBBAGB9FECBAYEAjCB/TAvBggrBgEFBQcCARYjaHR0
cDovL3d3dy50cnVzdDI0MDguY29tL3JlcG9zaXRvcnkwgckGCCsGAQUFBwICMIG8
MAwWBURhbklEMAMCAQEagatEYW5JRCB0ZXN0IGNlcnRpZmlrYXRlciBmcmEgZGVu
bmUgQ0EgdWRzdGVkZXMgdW5kZXIgT0lEIDEuMy42LjEuNC4xLjMxMzEzLjIuNC42
LjQuMi4gRGFuSUQgdGVzdCBjZXJ0aWZpY2F0ZXMgZnJvbSB0aGlzIENBIGFyZSBp
c3N1ZWQgdW5kZXIgT0lEIDEuMy42LjEuNC4xLjMxMzEzLjIuNC42LjQuMi4wgawG
A1UdHwSBpDCBoTA9oDugOYY3aHR0cDovL2NybC5zeXN0ZW10ZXN0MjIudHJ1c3Qy
NDA4LmNvbS9zeXN0ZW10ZXN0MjIxLmNybDBgoF6gXKRaMFgxCzAJBgNVBAYTAkRL
MRIwEAYDVQQKDAlUUlVTVDI0MDgxJTAjBgNVBAMMHFRSVVNUMjQwOCBTeXN0ZW10
ZXN0IFhYSUkgQ0ExDjAMBgNVBAMMBUNSTDM2MB8GA1UdIwQYMBaAFKuoAUQZsLND
mdr6fMzSABgD5zy/MB0GA1UdDgQWBBSNMd+ipv+Sfh/O01S7ntEj5AX5BjAJBgNV
HRMEAjAAMA0GCSqGSIb3DQEBCwUAA4IBAQAKAXMYlk4jVxa6bWDej4urxHcSZmUa
NG7uojcjBe81a3Lx0OMdUoQPojeatlRyQmrQU8l1JXUb/33utXn8GJZoJ7iMjmaX
S7Eie2pXKR0EbrIFW6MzB5fx3Yk7IQzkXgPAtCCuqLXF+cwRFwZaBBQ/spPx0Pg/
0gYxFWEklC99jmiiaCYF4T4N1x3L2fF0IfKJRY4aGGKuGY3xZwj2lGmrrcv1Nnih
l9BiCcK8wfufx4RxkGYn2RfUy6vP0FO3ZM164xfJW4iOI7ETS3EQyoZUVBhqeRzX
NXJz+wme3kb6HYR7bU3AzacyRIcWPAYQfZ9+WckLNklGp+vdn/ZwNjPo',
    ),
  ),
  'validate.authnrequest' => true,
  'saml20.sign.assertion' => true,
  'userid.attribute' => 'uid',
  'simplesaml.nameidattribute' => 'uid'
);

