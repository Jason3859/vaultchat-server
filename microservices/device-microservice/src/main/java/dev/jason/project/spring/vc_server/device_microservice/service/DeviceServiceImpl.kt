package dev.jason.project.spring.vc_server.device_microservice.service

import dev.jason.project.spring.vc_server.core.exception.VcException
import dev.jason.project.spring.vc_server.core.exception.VcException.DeviceException.DeviceAlreadyExistsException
import dev.jason.project.spring.vc_server.core.exception.VcException.DeviceException.DeviceNotFoundException
import dev.jason.project.spring.vc_server.core.model.Device
import dev.jason.project.spring.vc_server.core.service.DeleteDeviceService
import dev.jason.project.spring.vc_server.core.service.DeviceService
import dev.jason.project.spring.vc_server.core.service.GetDevicesService
import dev.jason.project.spring.vc_server.core.service.GetUserService
import dev.jason.project.spring.vc_server.device_microservice.model.DeviceEntity
import dev.jason.project.spring.vc_server.device_microservice.repo.DeviceRepository
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
