package dev.jason.project.spring.vc_server.microservice.device.model;

import dev.jason.project.spring.vc_server.core.model.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
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

	public Device asDevice() {
		return new Device(id, ownerUid, name, type, os, version, lastUsed);
	}

	public static DeviceEntity asEntity(Device device) {
		return new DeviceEntity(
			device.id() == null ? new ObjectId().toHexString() : device.id(),
			device.ownerUid(),
			device.name(),
			device.type(),
			device.os(),
			device.version(),
			device.lastUsed(),
			new LogoutMark(false, false)
		);
	}

    @ToString
    public static final class LogoutMark {

        private static final LogoutMark LOGOUT_MARK_CLEAR_MESSAGES = new LogoutMark(true, true);
        private static final LogoutMark LOGOUT_MARK_NO_CLEAR_MESSAGES = new LogoutMark(true, false);

        private final boolean isMarked;
        private final boolean clearMessages;

        private LogoutMark(boolean isMarked, boolean clearMessages) {
            this.isMarked = isMarked;
            this.clearMessages = clearMessages;
        }

        public static LogoutMark getLogoutMarkClearMessages() {
            return LOGOUT_MARK_CLEAR_MESSAGES;
        }

        public static LogoutMark getLogoutMarkNoClearMessages() {
            return LOGOUT_MARK_NO_CLEAR_MESSAGES;
        }

        public static LogoutMark getInstance(boolean clearMessages) {
            return clearMessages ? getLogoutMarkClearMessages() : getLogoutMarkNoClearMessages();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (LogoutMark) obj;
            return this.isMarked == that.isMarked &&
                this.clearMessages == that.clearMessages;
        }

        @Override
        public int hashCode() {
            return Objects.hash(isMarked, clearMessages);
        }
    }
}
