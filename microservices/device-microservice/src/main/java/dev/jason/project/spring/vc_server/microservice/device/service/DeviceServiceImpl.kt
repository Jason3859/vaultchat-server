package dev.jason.project.spring.vc_server.microservice.device.service

import dev.jason.project.spring.vc_server.core.exception.VcException
import dev.jason.project.spring.vc_server.core.exception.VcException.DeviceException.DeviceAlreadyExistsException
import dev.jason.project.spring.vc_server.core.model.Device
import dev.jason.project.spring.vc_server.core.service.*
import dev.jason.project.spring.vc_server.microservice.device.model.DeviceEntity
import dev.jason.project.spring.vc_server.microservice.device.repo.DeviceRepository
import org.springframework.stereotype.Service

@Service
class DeviceServiceImpl(
    private val deviceRepository: DeviceRepository,
    private val getUserService: GetUserService
) : DeviceService,
    GetDevicesService by GetDevicesServiceImpl(deviceRepository),
    DeleteDeviceService by DeleteDeviceServiceImpl(deviceRepository) {

    override fun addDevice(device: Device): Device {
        getUserService.getUserByUid(device.ownerUid)
            .let { if (it == null) throw VcException.UserException.UserNotFoundException() }

        val devices = deviceRepository.findByOwnerUid(device.ownerUid)
        val entity = DeviceEntity.asEntity(device)

        if (devices.contains(entity)) throw DeviceAlreadyExistsException()
        deviceRepository.save(entity)
        return entity.asDevice()
    }

    override fun markDeviceForLogout(device: Device, clearMessages: Boolean) {
        val entity = deviceRepository.findById(device.token)
            .orElseThrow { VcException.DeviceException.DeviceNotFoundException() }

        @Suppress("UsePropertyAccessSyntax") // having issues with lombok. that's why.
        entity.setLogoutMark(DeviceEntity.LogoutMark(true, clearMessages))
        deviceRepository.save(entity)
    }
}
