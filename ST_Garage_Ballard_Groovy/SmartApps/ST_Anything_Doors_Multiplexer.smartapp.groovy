/**
 *  ST_Anything Doors Multiplexer - ST_Anything_Doors_Multiplexer.smartapp.groovy
 *
 *  Copyright 2015 Daniel Ogorchock
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Change History:
 *
 *    Date        Who            What
 *    ----        ---            ----
 *    2015-01-10  Dan Ogorchock  Original Creation
 *    2015-01-11  Dan Ogorchock  Reduced unnecessary chatter to the virtual devices
 *    2015-01-18  Dan Ogorchock  Added support for Virtual Temperature/Humidity Device
 *    2016-08-07  John Duksta    Pared down for just a single garage door
 *
 */

definition(
    name: "ST_Ballard_Garage Doors Multiplexer",
    namespace: "ducksauz",
    author: "John Duksta",
    description: "Connects single Arduino with a DoorControl device and a ContactSensor device to their virtual device counterparts.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
	section("Select the Garage Doors (Virtual Door Control devices)") {
		input "garagedoor", title: "Left Garage Door", "capability.doorControl"
	}

	section("Select the Arduino ST_Anything_Doors device") {
		input "arduino", "capability.contactSensor"
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	subscribe()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	subscribe()
}

def subscribe() {

    subscribe(arduino, "garageDoor.open", garageDoorOpen)
    subscribe(arduino, "garageDoor.opening", garageDoorOpening)
    subscribe(arduino, "garageDoor.closed", garageDoorClosed)
    subscribe(arduino, "garageDoor.closing", garageDoorClosing)
    subscribe(garagedoor, "buttonPress.true", garageDoorPushButton)
}

// --- Garage Door ---
def garageDoorOpen(evt)
{
    if (garagedoor.currentValue("contact") != "open") {
    	log.debug "arduinoevent($evt.name: $evt.value: $evt.deviceId)"
    	garagedoor.open()
	}
}

def garageDoorOpening(evt)
{
    if (garagedoor.currentValue("contact") != "opening") {
	    log.debug "arduinoevent($evt.name: $evt.value: $evt.deviceId)"
    	garagedoor.opening()
	}
}

def garageDoorClosing(evt)
{
    if (garagedoor.currentValue("contact") != "closing") {
	    log.debug "arduinoevent($evt.name: $evt.value: $evt.deviceId)"
    	garagedoor.closing()
	}
}

def garageDoorClosed(evt)
{
    if (garagedoor.currentValue("contact") != "closed") {
	    log.debug "arduinoevent($evt.name: $evt.value: $evt.deviceId)"
    	garagedoor.close()
	}
}

def garageDoorPushButton(evt)
{
    log.debug "virtualGarageDoor($evt.name: $evt.value: $evt.deviceId)"
    arduino.pushLeft()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
}
