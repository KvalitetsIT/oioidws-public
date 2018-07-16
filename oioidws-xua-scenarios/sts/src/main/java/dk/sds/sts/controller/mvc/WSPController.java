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
import dk.sds.sts.controller.mvc.dto.viewform.WSPForm;
import dk.sds.sts.dao.model.WebServiceProvider;
import dk.sds.sts.service.WSPService;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
public class WSPController {

	@Autowired
	private WSPService wspService;

	@RequestMapping(value = "/wsp/list")
	public String list(Model model) {
		model.addAttribute("wspList", wspService.findAll());
		return "wsp/list";
	}

	@RequestMapping(value = "/wsp/view/{id}", method = RequestMethod.GET)
	public String view(Model model, @PathVariable("id") long id) {
		WebServiceProvider wsp = wspService.getById(id);
		if (wsp == null) {
			return "redirect:./list";
		}

		model.addAttribute("wsp", wsp);

		return "wsp/view";
	}

	@RequestMapping(value = "/wsp/edit/{id}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable("id") long id) {
		WebServiceProvider wsp = wspService.getById(id);
		if (wsp == null) {
			return "redirect:./list";
		}

		model.addAttribute("wsp", wsp);

		return "wsp/edit";
	}

	@RequestMapping(value = "/wsp/edit", method = RequestMethod.POST)
	public ResponseEntity<String> editWSPAsync(WSPForm wspForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}

		WebServiceProvider wsp = wspService.getById(Long.parseLong(wspForm.getId()));
		if (wsp == null) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}
		
		if (wspForm.getCertificate() != null) {
			X509Certificate certificate = null;
			String subject = null;
			byte[] rawCertificate = Base64.getDecoder().decode(wspForm.getCertificate());

			try {
				certificate = (X509Certificate) CertificateFactory.getInstance("x.509").generateCertificate(new ByteArrayInputStream(rawCertificate));
				subject = certificate.getSubjectDN().getName();
			} catch (CertificateException e) {
				log.error("Failed to parse certificate", e);
				return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
			}
	
			wsp.setCertificate(rawCertificate);
			wsp.setSubject(subject);
		}


		wsp.setName(wspForm.getName());
		wsp.setEntityId(wspForm.getEntityId());
		wspService.save(wsp);

		return new ResponseEntity<>("", HttpStatus.OK);
	}

	@RequestMapping(value = "/wsp/new", method = RequestMethod.POST)
	public String newWSC(Model model, WSPForm wspForm) throws Exception {
		byte[] rawCertificate = Base64.getDecoder().decode(wspForm.getCertificate());

		X509Certificate certificate = (X509Certificate) CertificateFactory.getInstance("x.509").generateCertificate(new ByteArrayInputStream(rawCertificate));
		String subject = certificate.getSubjectDN().getName();

		WebServiceProvider newWsp = new WebServiceProvider();
		newWsp.setName(wspForm.getName());
		newWsp.setEntityId(wspForm.getEntityId());
		
		newWsp.setCertificate(rawCertificate);
		newWsp.setSubject(subject);
		
		wspService.save(newWsp);

		model.addAttribute("wspList", wspService.findAll());
		return "redirect:./list";
	}

	@RequestMapping(value = "/wsp/delete/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		WebServiceProvider wsp = wspService.getById(id);
		if (wsp == null) {
			return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
		}

		wspService.delete(wsp.getId());

		return new ResponseEntity<>("", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/wsp/processCertificate", method = RequestMethod.POST)
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

}
