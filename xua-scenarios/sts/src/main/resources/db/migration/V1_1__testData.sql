INSERT INTO web_service_provider (id, name, entity_id, subject, certificate) VALUES(1, 'Demo Service', 'http://localhost:8080/service/hello', 'SERIALNUMBER=CVR:33257872-FID:58249312 + CN=IDWS XUA Test WSP (funktionscertifikat), O=Sundhedsdatastyrelsen // CVR:33257872, C=DK', UNHEX('308206263082050EA0030201020204591BDA39300D06092A864886F70D01010B05003048310B300906035504061302444B31123010060355040A0C095452555354323430383125302306035504030C1C5452555354323430382053797374656D746573742058584949204341301E170D3138303431303035353734375A170D3231303431303035353732365A308191310B300906035504061302444B312E302C060355040A0C2553756E64686564736461746173747972656C73656E202F2F204356523A333332353738373231523020060355040513194356523A33333235373837322D4649443A3538323439333132302E06035504030C274944575320585541205465737420575350202866756E6B74696F6E73636572746966696B61742930820122300D06092A864886F70D01010105000382010F003082010A0282010100AD2714ADA390748BCB07C9934A410DB44F11992B18BF7D71089511EAF76AC0CCB35D8BC959B64D5CE4A26D69BBCC8B00CD4B778652A4E5CFD9203CE48AA68726B5046DFE9A773707A6C7602639B5AC10CD9863034DD1F9B7C16294F8B2624A6BE6B3949C3EE592FAB50AA6BB73D54CF83DDF9EE4B70D07553656A26E5EA3B430FE79828BE7C839786A5FA4A3E716F6AF0CF4B62B46C2525944BB0A698ED91811D61B124091D6F5B7445BC3FF0D30337F9996C04E2B600CF87E3BBC5CB1131F6298FD0482D6E270A5B1AE1958B995A88107E21F32833378F5689CEBF8009FCB6527224A0DAD2CE9A826B2B9BDFE89B120E4DD739493BAF72425E0906D19EDF36B0203010001A38202CC308202C8300E0603551D0F0101FF0404030203B830819706082B0601050507010104818A308187303C06082B060105050730018630687474703A2F2F6F6373702E73797374656D7465737432322E7472757374323430382E636F6D2F726573706F6E646572304706082B06010505073002863B687474703A2F2F662E6169612E73797374656D7465737432322E7472757374323430382E636F6D2F73797374656D7465737432322D63612E636572308201200603551D2004820117308201133082010F060D2B0601040181F45102040604023081FD302F06082B060105050702011623687474703A2F2F7777772E7472757374323430382E636F6D2F7265706F7369746F72793081C906082B060105050702023081BC300C160544616E494430030201011A81AB44616E4944207465737420636572746966696B61746572206672612064656E6E6520434120756473746564657320756E646572204F494420312E332E362E312E342E312E33313331332E322E342E362E342E322E2044616E49442074657374206365727469666963617465732066726F6D2074686973204341206172652069737375656420756E646572204F494420312E332E362E312E342E312E33313331332E322E342E362E342E322E3081AC0603551D1F0481A43081A1303DA03BA0398637687474703A2F2F63726C2E73797374656D7465737432322E7472757374323430382E636F6D2F73797374656D746573743232312E63726C3060A05EA05CA45A3058310B300906035504061302444B31123010060355040A0C095452555354323430383125302306035504030C1C5452555354323430382053797374656D746573742058584949204341310E300C06035504030C0543524C3336301F0603551D23041830168014ABA8014419B0B34399DAFA7CCCD2001803E73CBF301D0603551D0E04160414A47A5A4014486230FC2BD219C735CA4E2A81B49C30090603551D1304023000300D06092A864886F70D01010B050003820101006B915C91BC2BAAAB474DEEC425AFF4AD3B561C24DBD4483332609D693C6FBBCFC5B33469F215BFA29DBA728E4101E38AE86EBDDA930435F97522D82B22D44BC57E0DF625713562E176B70DEFAAA293D7D17EF657EB3E089C32756DF2469A413553A167765E4B59ECD23FF699953DD662AE247D1AE1BC03EDA855795CB30FFA6A160B57ACBA4DF14C898E8665927B5D2E7787F5D6C4ADE7B6DEE298EE7D591B1D8E6051AC35492F71D9153EC4CD7CE7CB93AB834B29C118377D6BF14376FC4DABBE609743A7C0AE7698718276D1C8D0DF3526FAE15AFA772C8EFC335EB58139A1CC1FCBABC0395008A0BDFAD97A3BBF962E8D1C834CACA9CC5A168FD34C75DC7C'));

INSERT INTO web_service_consumer (id, name, subject, certificate) VALUES(1, 'WSC', 'SERIALNUMBER=CVR:33257872-FID:22933922 + CN=IDWS XUA Test WSC (funktionscertifikat), O=Sundhedsdatastyrelsen // CVR:33257872, C=DK', UNHEX('308206263082050EA0030201020204591BDA37300D06092A864886F70D01010B05003048310B300906035504061302444B31123010060355040A0C095452555354323430383125302306035504030C1C5452555354323430382053797374656D746573742058584949204341301E170D3138303431303035353634315A170D3231303431303035353534315A308191310B300906035504061302444B312E302C060355040A0C2553756E64686564736461746173747972656C73656E202F2F204356523A333332353738373231523020060355040513194356523A33333235373837322D4649443A3232393333393232302E06035504030C274944575320585541205465737420575343202866756E6B74696F6E73636572746966696B61742930820122300D06092A864886F70D01010105000382010F003082010A02820101009386BAD41F6B0F95859B47B707EFA0C4DED86CEC7FCA6C3EF8504F1761B1744A9ED268F878813030A2788C0E4926DE5650B48B7462A3BC964AFC66EAF7C507BD6C13BC33ED17AF98D60CA922D2B0821A558092715B0BCCE9FC5C603D3337E3078A54A4E1935B14AD78AC984D86FB219DF98ED557A3553289CE6918DA448600AC3D69D556A594956C983AEAD4F4CF7647C86791B12010EFDA65DC995C7E0F88675DAFC79AC1900B4710F233BEB651CCB1CE78C911EFE8EAC1B9E99A0A4B1AD75263DE04966E2897D3106C59EA506C379FD46FBE4EC0AF70C0D7E13BD5C121845C00FEC3F0CA88EACFB558011F2A645F37845C1EFCD7B60CBD1A57DE8AA3F5CB5F0203010001A38202CC308202C8300E0603551D0F0101FF0404030203B830819706082B0601050507010104818A308187303C06082B060105050730018630687474703A2F2F6F6373702E73797374656D7465737432322E7472757374323430382E636F6D2F726573706F6E646572304706082B06010505073002863B687474703A2F2F662E6169612E73797374656D7465737432322E7472757374323430382E636F6D2F73797374656D7465737432322D63612E636572308201200603551D2004820117308201133082010F060D2B0601040181F45102040604023081FD302F06082B060105050702011623687474703A2F2F7777772E7472757374323430382E636F6D2F7265706F7369746F72793081C906082B060105050702023081BC300C160544616E494430030201011A81AB44616E4944207465737420636572746966696B61746572206672612064656E6E6520434120756473746564657320756E646572204F494420312E332E362E312E342E312E33313331332E322E342E362E342E322E2044616E49442074657374206365727469666963617465732066726F6D2074686973204341206172652069737375656420756E646572204F494420312E332E362E312E342E312E33313331332E322E342E362E342E322E3081AC0603551D1F0481A43081A1303DA03BA0398637687474703A2F2F63726C2E73797374656D7465737432322E7472757374323430382E636F6D2F73797374656D746573743232312E63726C3060A05EA05CA45A3058310B300906035504061302444B31123010060355040A0C095452555354323430383125302306035504030C1C5452555354323430382053797374656D746573742058584949204341310E300C06035504030C0543524C3336301F0603551D23041830168014ABA8014419B0B34399DAFA7CCCD2001803E73CBF301D0603551D0E041604148D31DFA2A6FF927E1FCED354BB9ED123E405F90630090603551D1304023000300D06092A864886F70D01010B050003820101000A017318964E235716BA6D60DE8F8BABC4771266651A346EEEA2372305EF356B72F1D0E31D52840FA2379AB65472426AD053C97525751BFF7DEEB579FC18966827B88C8E66974BB1227B6A57291D046EB2055BA3330797F1DD893B210CE45E03C0B420AEA8B5C5F9CC1117065A04143FB293F1D0F83FD20631156124942F7D8E68A2682605E13E0DD71DCBD9F17421F289458E1A1862AE198DF16708F69469ABADCBF53678A197D06209C2BCC1FB9FC78471906627D917D4CBABCFD053B764CD7AE317C95B888E23B1134B7110CA865454186A791CD7357273FB099EDE46FA1D847B6D4DC0CDA7324487163C06107D9F7E59C90B364946A7EBDD9FF6703633E8'));

-- move these into a truststore somewhere else
INSERT INTO web_service_consumer (id, name, subject, certificate) VALUES(2, 'MOCES Certificate','CN=Thomas Ålbæk + SERIALNUMBER=CVR:34051178-RID:90897702, O=Digitaliseringsstyrelsen // CVR:34051178, C=DK', UNHEX('3082062D30820515A0030201020204591B8E81300D06092A864886F70D01010B05003048310B300906035504061302444B31123010060355040A0C095452555354323430383125302306035504030C1C5452555354323430382053797374656D746573742058584949204341301E170D3138303230323138313631385A170D3231303230323138313630325A307B310B300906035504061302444B3131302F060355040A0C284469676974616C69736572696E677373747972656C73656E202F2F204356523A33343035313137383139301506035504030C0E54686F6D617320C3856C62C3A66B3020060355040513194356523A33343035313137382D5249443A393038393737303230820122300D06092A864886F70D01010105000382010F003082010A028201010088E88CB0FEDDC35675865A802482359AED2F9F4D5E62D83A9E80DED5E21B42ABE020E37362B81F7A7DE2633E0BBFA14AAFB76C9989EF64F48DC5FB938E18AF4B5E93924FC384C9599DD2600ECE619FF02A49ED7202CD00AF839127DBEDA0FDEAACF3CE22768F520C12AE0A7CA6F8721872CDAA848796573C5CA51CEC43FBA478713D58389639498ECC1EDC288BE99A8DD9DE04E8C3F79E2BB032F86EE06B743B4A1AAC380026D40E5B2FB3D987F1AD154FD895067A36AC38D9B7C59703F606778E64685AC0ED8EDF68290140267E2F06FA59BC9FC9B2344A19DDAC06569BF8E93B0E6FCF86B45FC25F1ED7C3DF5F1D628F054B24E21A2D1BD1547FD67285BE910203010001A38202EA308202E6300E0603551D0F0101FF0404030203F830819706082B0601050507010104818A308187303C06082B060105050730018630687474703A2F2F6F6373702E73797374656D7465737432322E7472757374323430382E636F6D2F726573706F6E646572304706082B06010505073002863B687474703A2F2F6D2E6169612E73797374656D7465737432322E7472757374323430382E636F6D2F73797374656D7465737432322D63612E636572308201200603551D2004820117308201133082010F060D2B0601040181F45102040602053081FD302F06082B060105050702011623687474703A2F2F7777772E7472757374323430382E636F6D2F7265706F7369746F72793081C906082B060105050702023081BC300C160544616E494430030201011A81AB44616E4944207465737420636572746966696B61746572206672612064656E6E6520434120756473746564657320756E646572204F494420312E332E362E312E342E312E33313331332E322E342E362E322E352E2044616E49442074657374206365727469666963617465732066726F6D2074686973204341206172652069737375656420756E646572204F494420312E332E362E312E342E312E33313331332E322E342E362E322E352E301C0603551D110415301381116E656D6C6F67696E4064696773742E646B3081AC0603551D1F0481A43081A1303DA03BA0398637687474703A2F2F63726C2E73797374656D7465737432322E7472757374323430382E636F6D2F73797374656D746573743232312E63726C3060A05EA05CA45A3058310B300906035504061302444B31123010060355040A0C095452555354323430383125302306035504030C1C5452555354323430382053797374656D746573742058584949204341310E300C06035504030C0543524C3234301F0603551D23041830168014ABA8014419B0B34399DAFA7CCCD2001803E73CBF301D0603551D0E04160414E47FA5DAE59F7D022246AF44894DCD6A734DC71830090603551D1304023000300D06092A864886F70D01010B050003820101001AD891C1FFED3027064415D027DB160D2167A4254F8B07B47E36684A7713E6C4AD45AE65D3878196B43F570C7212A36701B24F80AC537427BC14AFB56C65FAFAA3F02707C4B35124268F0C4CAD165C03F9BCED4E60630F1247577176CFE999F926C44F786B25744D41F22441EFBB8BC8262F9B4CB4283122AB7F7D32D0FFF7636DA90079EEB411D6CA1BEB3602FDDEB0979C0DCACC55034D6E84523F123E7A36B5384B3541CCEC1D6F0D58DE43EEDD85F3059E29608F3CB80C72EC77542589301EB6C6D8A74A53E16FBEB43D0F8C30D13F5CB0F93DB26AD7B1200CB344879B4FCD70760E8F220919173051E8A5B71CCB9AE378E5FFCE92D8CF35DC72EC634317'));
INSERT INTO web_service_consumer (id, name, subject, certificate) VALUES(3, 'IdP', 'SERIALNUMBER=CVR:33257872-FID:93479418 + CN=IDWS XUA Test IdP (funktionscertifikat), O=Sundhedsdatastyrelsen // CVR:33257872, C=DK',  UNHEX('308206263082050EA0030201020204591BDA3D300D06092A864886F70D01010B05003048310B300906035504061302444B31123010060355040A0C095452555354323430383125302306035504030C1C5452555354323430382053797374656D746573742058584949204341301E170D3138303431303035353833355A170D3231303431303035353831365A308191310B300906035504061302444B312E302C060355040A0C2553756E64686564736461746173747972656C73656E202F2F204356523A333332353738373231523020060355040513194356523A33333235373837322D4649443A3933343739343138302E06035504030C274944575320585541205465737420496450202866756E6B74696F6E73636572746966696B61742930820122300D06092A864886F70D01010105000382010F003082010A028201010088C43611D92A601222093B384F82B9F2848897AEA3420A2C0C069B58D9CE81E95CDAC506578E56A043707894D8DC3E4127279DA72BE121DAF11E09B485E5311237FB92EC4409CC1267C6CE81ED72D0F443168870C48569918AE14D87C6511F7F86F0620C3A0A57DD86FB0B1EA67217189E8217A3998738926BEC3E561100270F85CEE9B8EF5092A300DBDB936C632C899CE985FEBD9477A43B7676E6F6F02D3B6F54CEF515F106BACCEFF3DE689474A1D7E66A82E0B05AC9BBAB6CA5F91D93B0AB1733FD09F5CEDAA9D13DD3DCB175C9FA99061C33CAF16D9816B1676EA0FDF8915AFB36C1666AD200C0C18BE0E870E240ABAF20AB5604688415BCD0EE26EB990203010001A38202CC308202C8300E0603551D0F0101FF0404030203B830819706082B0601050507010104818A308187303C06082B060105050730018630687474703A2F2F6F6373702E73797374656D7465737432322E7472757374323430382E636F6D2F726573706F6E646572304706082B06010505073002863B687474703A2F2F662E6169612E73797374656D7465737432322E7472757374323430382E636F6D2F73797374656D7465737432322D63612E636572308201200603551D2004820117308201133082010F060D2B0601040181F45102040604023081FD302F06082B060105050702011623687474703A2F2F7777772E7472757374323430382E636F6D2F7265706F7369746F72793081C906082B060105050702023081BC300C160544616E494430030201011A81AB44616E4944207465737420636572746966696B61746572206672612064656E6E6520434120756473746564657320756E646572204F494420312E332E362E312E342E312E33313331332E322E342E362E342E322E2044616E49442074657374206365727469666963617465732066726F6D2074686973204341206172652069737375656420756E646572204F494420312E332E362E312E342E312E33313331332E322E342E362E342E322E3081AC0603551D1F0481A43081A1303DA03BA0398637687474703A2F2F63726C2E73797374656D7465737432322E7472757374323430382E636F6D2F73797374656D746573743232312E63726C3060A05EA05CA45A3058310B300906035504061302444B31123010060355040A0C095452555354323430383125302306035504030C1C5452555354323430382053797374656D746573742058584949204341310E300C06035504030C0543524C3336301F0603551D23041830168014ABA8014419B0B34399DAFA7CCCD2001803E73CBF301D0603551D0E04160414E919C2AFB30F01F51C77679A3AFFD25C99393A9C30090603551D1304023000300D06092A864886F70D01010B05000382010100042004120B6FD057B054F33E8DDE0E592C75DDE97130EE6E5F3A2F4888A98E086C1827B6075ACA9263A43DE03F4CCA66DFF4E79315C5162CBBA33D37FECB9933AD9128442A75805417FCF23C96A33E98DB7C8C425EEE0BB43349E139DE707F694535170D775A03E5D7148CF8065F711B57142FAAF9ED45F152FD1A1C27FEFD3C9A418B4B34F59EC84BAD937E8809A845C886313EE412CB4638FF0FDCEE8D36945F63DF15829FBBC1C0BD4092A992A18E4AE04816DECD062717A4987796BBC1965FB014AB4391BD025C7E2ECE525660CADA544394383E25AA40207561077177586D500CCAD1DBDBAA1AF6D1A92A2101A86D3C6C55D18175C34E87C6F9C0B6EF8F'));

INSERT INTO granted_access (wsc_id, wsp_id) VALUES(1, 1);
