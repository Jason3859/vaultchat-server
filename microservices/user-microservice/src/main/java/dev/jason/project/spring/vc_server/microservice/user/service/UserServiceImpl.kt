package dev.jason.project.spring.vc_server.microservice.user.service

import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserAlreadyExistsException
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserNotFoundException
import dev.jason.project.spring.vc_server.core.model.User
import dev.jason.project.spring.vc_server.core.service.CheckIfUserIsBlockedService
import dev.jason.project.spring.vc_server.core.service.GetUserService
import dev.jason.project.spring.vc_server.core.service.InformUserStatusUpdateService
import dev.jason.project.spring.vc_server.core.service.UserService
import dev.jason.project.spring.vc_server.microservice.user.model.UserEntity
import dev.jason.project.spring.vc_server.microservice.user.repo.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val repository: UserRepository,
    private val informUserStatusUpdateService: InformUserStatusUpdateService,
    private val checkIfUserIsBlockedService: CheckIfUserIsBlockedService
) : UserService,
	GetUserService by GetUserServiceImpl(repository) {

    override fun addUser(user: User) {
        val entity = repository.findById(user.uid)
        if (entity.isPresent) throw UserAlreadyExistsException()

        repository.save(UserEntity.asEntity(user, System.currentTimeMillis()))
    }

    override fun deleteUser(uid: String) {
        val entity = repository.findById(uid)
        if (entity.isPresent) repository.delete(entity.get()) else throw UserNotFoundException()
    }

    @Suppress("UsePropertyAccessSyntax")
    override fun updateHeartBeat(uid: String) {
        val entity = repository.findById(uid).orElseThrow { UserNotFoundException() }

        entity.setLastHeartBeat(System.currentTimeMillis())
        entity.setStatus(User.Status.Online)

        repository.save(entity)

        informUserStatusUpdateService.sendStatusUpdate(uid, User.Status.Online)
    }

    @Suppress("UsePropertyAccessSyntax")
    override fun updateDisplayName(uid: String, displayName: String): User? {
        val entity = repository.findById(uid).orElseThrow { UserNotFoundException() }

        entity.setDisplayName(displayName)
        return repository.save(entity).asUser()
    }

    override fun searchUsers(fromUid: String, searchQuery: String): List<User> {
        return getAllUsersByDisplayName(searchQuery)
            .filter { it.uid != fromUid }
            .filter { !checkIfUserIsBlockedService.isUserBlocked(fromUid, it.uid) }
    }

    private fun getAllUsersByDisplayName(query: String): List<User> {
        return repository.findByDisplayNameContainingIgnoreCase(query).stream()
            .map { it.asUser() }
            .toList()
    }
}
