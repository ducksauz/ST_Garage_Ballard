/**
 *  ST_Anything_Doors Device Type - ST_Anything_Doors.device.groovy
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
 *    2016-08-07  John Duksta    Pared down to my use case for just one garage door
 *
 *
 */

metadata {
	definition (name: "ST_Garage_Ballard", namespace: "ducksauz", author: "John Duksta") {
		capability "Contact Sensor"
		capability "Sensor"

		attribute "garageDoor", "string"

		command "push"
	}

    simulator {
        status "on":  "catchall: 0104 0000 01 01 0040 00 0A21 00 00 0000 0A 00 0A6F6E"
        status "off": "catchall: 0104 0000 01 01 0040 00 0A21 00 00 0000 0A 00 0A6F6666"

        // reply messages
        reply "raw 0x0 { 00 00 0a 0a 6f 6e }": "catchall: 0104 0000 01 01 0040 00 0A21 00 00 0000 0A 00 0A6F6E"
        reply "raw 0x0 { 00 00 0a 0a 6f 66 66 }": "catchall: 0104 0000 01 01 0040 00 0A21 00 00 0000 0A 00 0A6F6666"
    }

	// tile definitions
	tiles {
		standardTile("Garage Door", "device.garageDoor", width: 1, height: 1, canChangeIcon: true, canChangeBackground: true) {
			state "closed", label: 'Closed', action: "push", icon: "st.doors.garage.garage-closed", backgroundColor: "#79b821", nextState: "closed"
            state "open", label: 'Open', action: "push", icon: "st.doors.garage.garage-open", backgroundColor: "#ffa81e", nextState: "open"
            state "opening", label: 'Opening', action: "push", icon: "st.doors.garage.garage-opening", backgroundColor: "89C2E8", nextState: "opening"
            state "closing", label: 'Closing', action: "push", icon: "st.doors.garage.garage-closing", backgroundColor: "89C2E8", nextState: "closing"
 		}

        main (["garageDoor"])
        details(["garageDoor"])
	}
}

//Map parse(String description) {
def parse(String description) {
    def msg = zigbee.parse(description)?.text
    log.debug "Parse got '${msg}'"

    def parts = msg.split(" ")
    def name  = parts.length>0?parts[0].trim():null
    def value = parts.length>1?parts[1].trim():null

    name = value != "ping" ? name : null

    def result = createEvent(name: name, value: value)

    log.debug result

    return result
}

def push() {
	log.debug "Executing 'push' = 'garageDoor on'"
    zigbee.smartShield(text: "garageDoor on").format()
}
