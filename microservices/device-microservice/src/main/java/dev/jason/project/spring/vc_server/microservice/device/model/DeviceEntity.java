package dev.jason.project.spring.vc_server.microservice.device.model;

import dev.jason.project.spring.vc_server.core.model.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "device")
@AllArgsConstructor
@Data
public class DeviceEntity {

	@Id private final String id;
	private final String ownerUid;
	private final String name;
	private final Device.Type type;
	private final Device.OS os;
	private final String version;
	private LocalDateTime lastUsed;
	private LogoutMark logoutMark;

	public record LogoutMark(boolean isMarked, boolean clearMessages) {}

	public Device asDevice() {
		return new Device(id, ownerUid, name, type, os, version, lastUsed);
	}

	public static DeviceEntity asEntity(Device device) {
		return new DeviceEntity(
			device.id(),
			device.ownerUid(),
			device.name(),
			device.type(),
			device.os(),
			device.version(),
			device.lastUsed(),
			new LogoutMark(false, false)
		);
	}

	public void setLogoutMark(LogoutMark logoutMark) {
		this.logoutMark = logoutMark;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DeviceEntity other)) return false;
		return Objects.equals(this.ownerUid, other.ownerUid) &&
			Objects.equals(this.name, other.name);
	}
}
