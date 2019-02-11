package com.pushy.talk.pushtalk.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Audio {
	@Id
	public ObjectId _id;

	public byte[] audioData;

	public Audio() {}

	public Audio(byte[] audioData) {
		this.audioData = audioData;
	}

	public String get_id() { return _id.toHexString(); }
	public void set_id(ObjectId _id) { this._id = _id; }

	public byte[] getAudioData() { return audioData; }
	public void setAudioData(byte[] audioData) { this.audioData = audioData; }
}