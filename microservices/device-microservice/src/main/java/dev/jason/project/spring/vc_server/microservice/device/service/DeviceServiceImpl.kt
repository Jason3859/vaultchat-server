package dev.jason.project.spring.vc_server.microservice.device.service

import dev.jason.project.spring.vc_server.core.exception.VcException
import dev.jason.project.spring.vc_server.core.exception.VcException.DeviceException.DeviceAlreadyExistsException
import dev.jason.project.spring.vc_server.core.exception.VcException.DeviceException.DeviceNotFoundException
import dev.jason.project.spring.vc_server.core.model.Device
import dev.jason.project.spring.vc_server.core.service.*
import dev.jason.project.spring.vc_server.microservice.device.model.DeviceEntity
import dev.jason.project.spring.vc_server.microservice.device.repo.DeviceLogoutQueueRepository
import dev.jason.project.spring.vc_server.microservice.device.repo.DeviceRepository
import org.springframework.stereotype.Service

@Service
class DeviceServiceImpl(
    deviceLogoutQueueRepository: DeviceLogoutQueueRepository,
    getDevicesService: GetDevicesService,
    private val deviceRepository: DeviceRepository,
    private val getUserService: GetUserService
) : DeviceService,
    GetDevicesService by GetDevicesServiceImpl(deviceRepository),
    DeleteDeviceService by DeleteDeviceServiceImpl(deviceRepository),
    LogoutDeviceService by LogoutDeviceServiceImpl(deviceLogoutQueueRepository, getDevicesService) {

    override fun addDevice(device: Device): Device {
        getUserService.getUserByUid(device.ownerUid)
            .let { if (it == null) throw VcException.UserException.UserNotFoundException() }

        val devices = deviceRepository.findByOwnerUid(device.ownerUid)

        val entity = DeviceEntity.asEntity(device)

        if (devices.contains(entity)) {
            throw DeviceAlreadyExistsException()
        }

        deviceRepository.save(entity)

        return entity.asDevice()
    }

    override fun verifyDevice(device: Device) {
        val devices: MutableList<Device?> = getDevicesByOwnerUid(device.ownerUid)

        if (!devices.contains(device)) {
            throw DeviceNotFoundException()
        }
    }
}
