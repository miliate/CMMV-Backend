package mz.org.fgh.cmmv.backend.distribuicaoAdministrativa

import grails.converters.JSON
import grails.rest.RestfulController
import grails.validation.ValidationException
import mz.org.fgh.cmmv.backend.appointment.Appointment
import mz.org.fgh.cmmv.backend.utilities.JSONSerializer

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

class DistrictController extends RestfulController{

    DistrictService districtService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    DistrictController() {
        super(District)
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

       // JSON.use('deep'){
      //      render districtService.list(params) as JSON
       // }
        render JSONSerializer.setObjectListJsonResponse(districtService.list(params)) as JSON
    }

    def show(Long id) {
     //   JSON.use('deep'){
     //       render districtService.get(id) as JSON
      //  }
        render JSONSerializer.setJsonObjectResponse(districtService.get(id)) as JSON
    }

    @Transactional
    def save(District district) {
        if (district == null) {
            render status: NOT_FOUND
            return
        }
        if (district.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond district.errors
            return
        }

        try {
            districtService.save(district)
        } catch (ValidationException e) {
            respond district.errors
            return
        }

        respond district, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(District district) {
        if (district == null) {
            render status: NOT_FOUND
            return
        }
        if (district.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond district.errors
            return
        }

        try {
            districtService.save(district)
        } catch (ValidationException e) {
            respond district.errors
            return
        }

        respond district, [status: OK, view:"show"]
    }

    @Transactional
    def delete(Long id) {
        if (id == null || districtService.delete(id) == null) {
            render status: NOT_FOUND
            return
        }

        render status: NO_CONTENT
    }
}
