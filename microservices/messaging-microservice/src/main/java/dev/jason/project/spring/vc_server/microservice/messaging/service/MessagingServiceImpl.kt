package dev.jason.project.spring.vc_server.microservice.messaging.service

import dev.jason.project.spring.vc_server.core.exception.VcException.MessagingException.MessageTextBlankException
import dev.jason.project.spring.vc_server.core.exception.VcException.SocialException.BlockedByUserException
import dev.jason.project.spring.vc_server.core.model.Message
import dev.jason.project.spring.vc_server.core.service.*
import dev.jason.project.spring.vc_server.microservice.messaging.repo.messaging.MessagingRepository
import org.springframework.stereotype.Service

@Service
class MessagingServiceImpl(
    private val messagingRepository: MessagingRepository,
    private val getUserService: GetUserService,
    private val checkIfUserIsBlockedService: CheckIfUserIsBlockedService,
    private val connectionsService: ConnectionsService,
    private val getDevicesService: GetDevicesService,
    private val messageQueueService: MessageQueueService
) : MessagingService,
	SendLogoutRequestService by SendLogoutRequestServiceImpl(messagingRepository),
    InformUserStatusUpdateService by InformUserStatusUpdateServiceImpl(messagingRepository, connectionsService, getDevicesService) {

    override fun sendMessage(message: Message) {
        if (message.text.isBlank()) {
            throw MessageTextBlankException()
        }

        val from = getUserService.getUserByUid(message.from)
        val to = getUserService.getUserByUid(message.to)

        if (checkIfUserIsBlockedService.isUserBlocked(message.from, message.to)) {
            throw BlockedByUserException()
        }

        connectionsService.connect(from.uid, to.uid)

        val devicesOfFirstUser = getDevicesService.getDevicesByOwnerUid(from.uid)
        val devicesOfSecondUser = getDevicesService.getDevicesByOwnerUid(to.uid)

        if (devicesOfSecondUser.isEmpty()) {
            messageQueueService.addMessageToQueue(to.uid, message)
            return
        }

        (devicesOfFirstUser + devicesOfSecondUser).forEach { device ->
            messagingRepository.sendMessage(message, device)
        }
    }
}
