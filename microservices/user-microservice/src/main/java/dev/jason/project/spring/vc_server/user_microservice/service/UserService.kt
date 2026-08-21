package dev.jason.project.spring.vc_server.user_microservice.service

import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserAlreadyExistsException
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserNotFoundException
import dev.jason.project.spring.vc_server.core.model.Device
import dev.jason.project.spring.vc_server.core.model.User
import dev.jason.project.spring.vc_server.core.service.*
import dev.jason.project.spring.vc_server.user_microservice.model.UserEntity
import dev.jason.project.spring.vc_server.user_microservice.repo.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repository: UserRepository,
    private val sendLogoutRequestService: SendLogoutRequestService,
    private val informUserStatusUpdateService: InformUserStatusUpdateService,
    private val deleteDeviceService: DeleteDeviceService,
    private val checkIfUserIsBlockedService: CheckIfUserIsBlockedService
) : GetUserService by GetUserServiceImpl(repository) {

    fun addUser(user: User) {
        val entity = repository.findById(user.uid)

        if (entity.isPresent) {
            throw UserAlreadyExistsException()
        }

        repository.save(UserEntity.asEntity(user))
    }

    fun deleteUser(uid: String) {
        val entity = repository.findById(uid)

        if (entity.isPresent) {
            repository.delete(entity.get())
            return
        }

        throw UserNotFoundException()
    }

    fun updateHeartBeat(uid: String) {
        val entity = repository.findById(uid)

        if (entity.isEmpty) {
            throw UserNotFoundException()
        }

        val e = entity.get()
        e.lastHeartBeat = System.currentTimeMillis()
        e.status = User.Status.Online
        repository.save(e)

        informUserStatusUpdateService.sendStatusUpdate(uid, User.Status.Online)
    }

    fun getAllUsersByDisplayName(query: String?): List<User> {
        return repository.findByDisplayNameContainingIgnoreCase(query).stream()
            .map { obj: UserEntity? -> obj!!.asUser() }
            .toList()
    }

    fun logout(device: Device?, clearMessages: Boolean) {
        sendLogoutRequestService.sendLogoutRequest(device, clearMessages)
        deleteDeviceService.deleteDevice(device)
    }

    fun searchUsers(fromUid: String, searchQuery: String): List<User> {
        return getAllUsersByDisplayName(searchQuery)
            .filter { it.uid != fromUid }
            .filter { !checkIfUserIsBlockedService.isUserBlocked(fromUid, it.uid) }
    }
}
