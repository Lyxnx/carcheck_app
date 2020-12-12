package net.lyxnx.carcheck.rest.response

import net.lyxnx.carcheck.rest.model.Vehicle

class GetVehicleResponse : GenericResponse() {
    var vehicle: Vehicle? = null
}