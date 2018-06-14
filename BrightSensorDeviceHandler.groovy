/**
 *  Brightswitch Name, Motion, Temp, and Light Sensor, switch name, software version, a modified motion sensor from Exarlabs
 *   Credit to original SmartApp/deviceHandler developed by ExarLabs, atiyka, and Attila Szasz
 *  Copyright 2018 Shawn McClung
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
 */
metadata {
	definition (name: "Bright Sensors2", namespace: "spinn360", author: "Shawn McClung") {
		capability "Motion Sensor"
		capability "Temperature Measurement"
		capability "Illuminance Measurement"
	}

	simulator {
		//status "active": "bright motion"
		//status "inactive": "bright no motion"
	}

	tiles(scale: 2) {
		multiAttributeTile(name:"motion", type: "generic", width: 6, height: 4){
			tileAttribute("device.motion", key: "PRIMARY_CONTROL") {
				attributeState("active", label:'motion', icon:"st.motion.motion.active", backgroundColor:"#00A0DC")
				attributeState("inactive", label:'no motion', icon:"st.motion.motion.inactive", backgroundColor:"#CCCCCC")
			}
 		}

		valueTile("Nam", "device.Nam", decoration: "flat", width: 3, height: 1){
				state("default", label: '${currentValue}')
		}
		valueTile("sf", "device.sf", decoration: "flat", width: 3, height: 1){
				state("default", label: '${currentValue}')
		}
	//	valueTile("lastMovement", "device.lastMovement", decoration: "flat", width: 6, height: 2){
	//			state("default", label: '${currentValue}')
	//	}

		valueTile("temp", "device.temp", decoration: "flat", width: 2, height: 2) {
					state( "default", label: '${currentValue}°',
						backgroundColors:[
							[value: 69, color: "#153591"],
							[value: 70, color: "#1e9cbb"],
							[value: 71, color: "#90d2a7"],
							[value: 74, color: "#44b621"],
							[value: 80, color: "#f1d801"],
							[value: 85, color: "#d04e00"],
							[value: 90, color: "#bc2323"]
						]
					)
		}
		valueTile("light", "device.light", decoration: "flat", width: 2, height: 2){
				state("default", label: 'Lux:\n${currentValue}')

		}


		main "motion"
		details "motion", "Nam", "sf", "temp", "light"
	}
}

def parse(String description) {

}

// Parse incoming device messages to generate events
def parse(Map params) {
	log.debug "Parsing '${params}'"
    //log.debug "parsing value '${params["value"]}'"
		if(params["value"] == "active") {
    	movement()
    } else {
    	noMovement()
    }

		def temp = params["temp"]

		log.debug "temp is now $temp"
		def light = params["light"]
    log.debug "light is now $light"
    log.debug "sendEvent name: temp, value: $temp"
		def name = params["name"]
		log.debug "switchname is $name"
		def sf = params["sf"]
		log.debug "software version $sf"
	//	def last = params["time"]
	//	log.debug "lastMovement $last"

	//	if(last != null){
	//		sendEvent(name: "lastMovement", value: last)
	//	}
    //createEvent(name: "temp", value: $temp)  <--This doesn't work
		if(name != null){
			sendEvent(name: "Nam", value: name)
		}
		if(sf != null){
			sendEvent(name: "sf", value: sf)
		}
    if(temp != null){
    def newtemp = temp.toInteger()
    log.debug "Temp $temp changed to newtemp $newtemp"
    sendEvent(name:"temp", value: newtemp)
}
		log.debug "sendEvent name: light, value: $light"
	if(light != null){
    def newlight = light.toInteger() * 4
    log.debug "light $light changed to newlight $newlight"
		sendEvent(name:"light", value: newlight)
}

}

def movement() {
	sendEvent(
		name: "motion",
		value: "active"
	)
}

def noMovement() {
	sendEvent(
		name: "motion",
		value: "inactive"
	)
}
