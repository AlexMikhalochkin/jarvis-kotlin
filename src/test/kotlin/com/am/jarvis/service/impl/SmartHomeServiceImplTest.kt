package com.am.jarvis.service.impl

import com.am.momomo.connector.api.DeviceProvider
import com.am.momomo.connector.api.DeviceStateChanger
import com.am.momomo.connector.api.DeviceStateProvider
import com.am.momomo.model.Device
import com.am.momomo.model.DeviceState
import com.am.momomo.notifier.api.Notifier
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Verification for [SmartHomeServiceImpl].
 *
 * @author Alex Mikhalochkin
 */
@ExtendWith(MockKExtension::class)
internal class SmartHomeServiceImplTest {

    private val deviceId = "first"

    @MockK(relaxUnitFun = true)
    lateinit var notifier: Notifier
    @MockK
    lateinit var deviceProvider: DeviceProvider
    @MockK
    lateinit var deviceStateProvider: DeviceStateProvider
    @MockK
    lateinit var deviceStateChanger: DeviceStateChanger
    @MockK(relaxUnitFun = true)
    lateinit var repository: TestRepository

    private lateinit var service: SmartHomeServiceImpl

    @BeforeEach
    fun setUp() {
        val devices: List<Device> = listOf(mockk())
        every { deviceProvider.getAllDevices() } returns devices
        every { deviceStateProvider.getSource() } returns "MegaD"
        every { deviceStateChanger.getSource() } returns "MegaD"
        every { notifier.getSource() } returns "SMART_THINGS"
        service = SmartHomeServiceImpl(
            listOf(deviceProvider),
            listOf(deviceStateProvider),
            listOf(deviceStateChanger),
            listOf(notifier),
            repository
        )
    }

    @Test
    fun getAllDevices() {
        val devices: List<Device> = listOf(mockk())
        every { deviceProvider.getAllDevices() } returns devices

        assertEquals(devices, service.getAllDevices())
        verify { deviceProvider.getAllDevices() }
        verify { repository.save(devices) }
    }

    @Test
    fun getDeviceStates() {
        val deviceIds = listOf(deviceId, "second")
        val states = listOf(DeviceState(deviceId, true), DeviceState("second", false))
        every { deviceStateProvider.getDeviceStates(deviceIds) } returns states
        every { repository.test(deviceIds) } returns mapOf("MegaD" to deviceIds)

        assertEquals(states, service.getDeviceStates(deviceIds))
        verify { deviceStateProvider.getDeviceStates(deviceIds) }
    }

    @Test
    fun testChangeStates() {
        val states = listOf(DeviceState(deviceId, true), DeviceState("second", false))
        val deviceIds = listOf(deviceId, "second")
        every { repository.test(any()) } returns mapOf("MegaD" to deviceIds)
        every { deviceStateChanger.areNotificationsEnabled() } returns false
        every { deviceStateChanger.changeStates(states) } returns states

        service.changeStates(states, "YANDEX")

        verify {
            deviceStateChanger.changeStates(states)
            notifier.notify(states)
        }
    }
}
