package dev.jason.project.spring.vc_server.social_microservice.service

import dev.jason.project.spring.vc_server.core.exception.VcException.SocialException.*
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserAlreadyExistsException
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserNotFoundException
import dev.jason.project.spring.vc_server.core.model.User
import dev.jason.project.spring.vc_server.core.service.CheckIfUserIsBlockedService
import dev.jason.project.spring.vc_server.core.service.ConnectionsService
import dev.jason.project.spring.vc_server.core.service.GetUserService
import dev.jason.project.spring.vc_server.core.service.MessageQueueService
import dev.jason.project.spring.vc_server.social_microservice.model.SocialEntity
import dev.jason.project.spring.vc_server.social_microservice.repo.SocialRepository
import org.springframework.stereotype.Service

@Service
class SocialService(
    private val repository: SocialRepository,
    private val getUserService: GetUserService
) :
    CheckIfUserIsBlockedService by CheckIfUserIsBlockedServiceImpl(repository),
    ConnectionsService by ConnectionsServiceImpl(repository),
    MessageQueueService by MessageQueueServiceImpl(repository) {

    fun getBlockedUsers(uid: String?): List<User> {
        val entity = repository.findByUserId(uid)

        if (entity.isEmpty) throw UserNotFoundException()

        return entity.get()
            .blocklist
            .stream()
            .map { uid: String? -> getUserService.getUserByUid(uid) }
            .toList()
    }

    fun registerNewUser(uid: String?) {
        if (repository.findByUserId(uid).isPresent) {
            throw UserAlreadyExistsException()
        }

        val entity = SocialEntity(uid, emptyList(), emptyList(), emptyList())
        repository.save(entity)
    }

    fun block(uid1: String?, uid2: String?) {
        if (uid1 == uid2) {
            throw SelfBlockException()
        }

        val entity1 = getSocialEntityByUid(uid1)
        val entity2 = getSocialEntityByUid(uid2)

        if (entity1.blocklist.contains(entity2.userId)) {
            throw UserAlreadyBlockedException()
        }

        entity1.blocklist.add(uid2)
        entity1.connections.remove(uid2)

        repository.save(entity1)
    }

    fun unblock(uid1: String?, uid2: String?) {
        if (uid1 == uid2) {
            throw SelfUnblockException()
        }

        val entity1 = getSocialEntityByUid(uid1)

        val blocklist = entity1.blocklist

        if (blocklist.contains(uid2)) {
            blocklist.remove(uid2)
            repository.save(entity1)
            return
        }

        throw UserNotBlockedException()
    }

    fun getSocialEntityByUid(uid: String?): SocialEntity {
        val entity = repository.findByUserId(uid)

        if (entity.isEmpty) {
            throw UserNotFoundException()
        }

        return entity.get()
    }
}
