package com.am.jarvis.integration.impl.megad.mqtt

import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Mqtt client for MegaD.
 *
 * @author Alex Mikhalochkin
 */
@Component
class MegaDMqttClient(
    @Value("\${megad.id}") megadId: String,
    @Value("\${mqtt.server.url}") mqttServerUrl: String,
    callback: MqttCallback,
    options: MqttConnectOptions
) : MqttClient(mqttServerUrl, "jarvis") {

    private val outPorts = setOf(7, 8, 9, 10, 11, 12, 13, 22, 23, 24, 25, 26, 27, 28)

    init {
        this.setCallback(callback)
        this.connect(options)
        val topicFilters = outPorts
            .map { "$megadId/$it" }
            .toTypedArray()
        this.subscribe(topicFilters, IntArray(outPorts.size) { 0 })
    }
}
