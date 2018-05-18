package dk.sds.sts.controller.mvc;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dk.sds.sts.controller.mvc.dto.CertificateResponseDTO;
import dk.sds.sts.controller.mvc.dto.viewform.WSCForm;
import dk.sds.sts.dao.model.WebServiceConsumer;
import dk.sds.sts.dao.model.WebServiceProvider;
import dk.sds.sts.service.WSCService;
import dk.sds.sts.service.WSPService;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
public class WSCController {

	@Autowired
	private WSCService wscService;

	@Autowired
	private WSPService wspService;

	@RequestMapping(value = "/wsc/list")
	public String list(Model model) {
		model.addAttribute("wscList", wscService.findAll());
		return "wsc/list";
	}

	@RequestMapping(value = "/wsc/view/{id}", method = RequestMethod.GET)
	public String view(Model model, @PathVariable("id") long id) {
		WebServiceConsumer wsc = wscService.getById(id);
		if (wsc == null) {
			return "redirect:./list";
		}

		model.addAttribute("wsc", wsc);

		return "wsc/view";
	}

	@RequestMapping(value = "/wsc/edit/{id}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable("id") long id) {
		WebServiceConsumer wsc = wscService.getById(id);
		if (wsc == null) {
			return "redirect:./list";
		}

		model.addAttribute("wsc", wsc);
		model.addAttribute("wsps", wspService.findAll());

		return "wsc/edit";
	}

	@RequestMapping(value = "/wsc/edit", method = RequestMethod.POST)
	public ResponseEntity<String> editWSCAsync(WSCForm wscForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}

		WebServiceConsumer wsc = wscService.getById(Long.parseLong(wscForm.getId()));
		if (wsc == null) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}

		if(wscForm.getCertificate()!=null) {
			X509Certificate certificate = null;
			String subject = null;
			byte[] rawCertificate = Base64.getDecoder().decode(wscForm.getCertificate());
			try {
				certificate = (X509Certificate) CertificateFactory.getInstance("x.509").generateCertificate(new ByteArrayInputStream(rawCertificate));
				subject = certificate.getSubjectDN().getName();
			} catch (CertificateException e) {
				log.error("Failed to parse certificate", e);
				return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
			}
	
			wsc.setCertificate(rawCertificate);
			wsc.setSubject(subject);
		}

		wsc.setName(wscForm.getName());
		wscService.save(wsc);

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@RequestMapping(value = "/wsc/new", method = RequestMethod.POST)
	public String newWSC(Model model, WSCForm wscForm) throws Exception {
		byte[] rawCertificate = Base64.getDecoder().decode(wscForm.getCertificate());

		X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("x.509").generateCertificate(new ByteArrayInputStream(rawCertificate));
		String subject = certificate.getSubjectDN().getName();

		WebServiceConsumer newWsc = new WebServiceConsumer();
		newWsc.setCertificate(rawCertificate);
		newWsc.setName(wscForm.getName());
		newWsc.setSubject(subject);
		wscService.save(newWsc);

		model.addAttribute("wscList", wscService.findAll());
		return "redirect:./list";
	}

	@RequestMapping(value = "/wsc/processCertificate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> processCertificate(@RequestParam("certificateFile") MultipartFile certificateFile) {
		try {
			CertificateFactory certFactory = CertificateFactory.getInstance("x.509");
			X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(certificateFile.getInputStream());

			CertificateResponseDTO response = new CertificateResponseDTO();
			response.setCertificate(certificate.getEncoded());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.warn("Error occured while trying to process certificate: " + certificateFile);
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/wsc/delete/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		WebServiceConsumer wsc = wscService.getById(id);
		if (wsc == null) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}

		wscService.delete(wsc.getId());

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@RequestMapping(value = "/wsc/{id}/grantaccess/{wspid}", method = RequestMethod.POST)
	public ResponseEntity<?> grantAccess(@PathVariable("wspid") long wspid, @PathVariable("id") long id) {
		WebServiceProvider wsp = wspService.getById(wspid);
		if (wsp == null) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}
		WebServiceConsumer wsc = wscService.getById(id);
		if (wsc == null) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}

		wsc.getWebServiceProviders().add(wsp);
		wscService.save(wsc);

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@RequestMapping(value = "/wsc/{id}/denyaccess/{wspid}", method = RequestMethod.POST)
	public ResponseEntity<?> denyAccess(@PathVariable("wspid") long wspid, @PathVariable("id") long id) {
		WebServiceProvider wsp = wspService.getById(wspid);
		if (wsp == null) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}
		WebServiceConsumer wsc = wscService.getById(id);
		if (wsc == null) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}

		wsc.getWebServiceProviders().remove(wsp);
		wscService.save(wsc);

		return new ResponseEntity<>("", HttpStatus.OK);
	}
}
