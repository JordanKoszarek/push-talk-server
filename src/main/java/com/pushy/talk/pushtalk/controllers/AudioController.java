package com.pushy.talk.pushtalk.controllers;

import com.pushy.talk.pushtalk.models.Audio;
import com.pushy.talk.pushtalk.repositories.AudioRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/audio")
public class AudioController {
	@Autowired
	private AudioRepository audioRepository;

	private final static int THREE_MEGABYTE = 3000000;

	private final static Logger logger = LoggerFactory.getLogger(LoggerController.class);

	@RequestMapping(value = "/getaudio", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> getAllAudio(@RequestParam(value = "id") String audioID) {
		logger.info("Fetching audio.");
		if(StringUtils.isEmpty(audioID)){
			logger.warn("AudioID is null. Cannot fetch.");
			return ResponseEntity.badRequest().build();
		}

		Audio audio = audioRepository.findBy_id(new ObjectId(audioID));
		if(audio == null || audio.getAudioData() == null ||audio.getAudioData().length == 0){
			logger.warn("No audio with the ID {}.", audioID);
			return ResponseEntity.badRequest().build();
		}

		byte[] bytes = audio.getAudioData();
		InputStream inputStream = new ByteArrayInputStream(bytes);
		InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
		return ResponseEntity.ok().contentLength(bytes.length).contentType(MediaType.MULTIPART_FORM_DATA).body(inputStreamResource);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getAllAudioIDs() {
		logger.info("Fetching all audio ids.");
		List<Audio> audioList = audioRepository.findAll();
		if(audioList.size() > 0) {
			List<String> result = audioList.stream().map(Audio::get_id).collect(Collectors.toList());
			logger.info("Returning audio ids for {} recordings.", result.size());
			return ResponseEntity.ok().body(result);
		}
		logger.info("No audio ids found.");
		return ResponseEntity.ok().build();
	}



	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity modifyPetById(@RequestParam("file") MultipartFile file) {
		if(file != null) {
			try {
				byte[] audioData = file.getBytes();
				if (audioData.length > THREE_MEGABYTE) {
					logger.warn("Audio file is too large to upload.");
					return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
				}
				logger.info("Uploading audio file.");
				Audio audio = new Audio(audioData);
				audioRepository.save(audio);
				return ResponseEntity.status(HttpStatus.OK).build();
			}
			catch (Exception ex) {
				logger.warn("Upload audio failed.", ex);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		}
		logger.warn("Nothing to upload.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

}