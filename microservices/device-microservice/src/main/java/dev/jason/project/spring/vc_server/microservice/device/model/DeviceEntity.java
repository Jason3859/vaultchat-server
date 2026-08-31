package dev.jason.project.spring.vc_server.microservice.device.model;

import dev.jason.project.spring.vc_server.core.model.Device;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "device")
@AllArgsConstructor
@Getter
@Setter
public class DeviceEntity {

	private final String ownerUid;
	private final String name;
	private final Device.Type type;
	private final Device.OS os;
	private final String version;
	@Id
	private final String token;
	private LocalDateTime lastUsed;

	public Device asDevice() {
		return new Device(ownerUid, name, type, os, version, token, lastUsed);
	}

	public static DeviceEntity asEntity(Device device) {
		return new DeviceEntity(device.ownerUid(), device.name(), device.type(), device.os(), device.version(), device.token(), device.lastUsed());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj.getClass() != this.getClass()) {
			return false;
		}

		DeviceEntity that = (DeviceEntity) obj;

		return Objects.equals(this.name, that.getName());
	}
}
