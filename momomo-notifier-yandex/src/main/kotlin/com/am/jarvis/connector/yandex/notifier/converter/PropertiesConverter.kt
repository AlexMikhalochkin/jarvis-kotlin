package com.am.jarvis.connector.yandex.notifier.converter

import com.am.jarvis.controller.generated.model.Property
import com.am.jarvis.controller.generated.model.YandexState
import com.am.jarvis.core.model.DeviceState
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

/**
 * Converter for properties
 *
 * @author Alex Mikhalochkin
 */
@Component
class PropertiesConverter : Converter<DeviceState, List<Property>> {

    private val supportedProperties = listOf(
        SupportedProperty("humidity", "humidity", "devices.properties.float"),
        SupportedProperty("temperature", "temperature", "devices.properties.float"),
        SupportedProperty("battery", "battery_level", "devices.properties.float"),
        SupportedProperty("voltage", "voltage", "devices.properties.float"),
        SupportedProperty("button", "button", "devices.properties.event")
    )

    override fun convert(source: DeviceState): List<Property> {
        return supportedProperties.mapNotNull { convertProperty(source, it) }
    }

    private fun convertProperty(source: DeviceState, supportedProperty: SupportedProperty): Property? {
        val value = source.customData[supportedProperty.customDataName] ?: return null
        val yandexState = YandexState(supportedProperty.instance, value)
        return Property(yandexState, supportedProperty.propertyType)
    }

    private data class SupportedProperty(
        val customDataName: String,
        val instance: String,
        val propertyType: String
    )
}
