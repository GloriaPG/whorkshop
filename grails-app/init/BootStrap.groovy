import com.security.Requestmap
import com.security.Rol
import com.security.User
import com.security.UserRol

class BootStrap {

    def init = { servletContext ->
        // Creamos usuarios uno mortal y uno admin
        User admin= new User()
        admin.username="admin"
        admin.password="admin"
        admin.email="gp@vincoorbis.com"
        admin.save(flush:true, failOnError:true)

        User mortal= new User()
        mortal.username="mortal"
        mortal.password="mortal"
        mortal.email="gp@vincoorbis.com"
        mortal.save(flush:true, failOnError:true)

        // Crear los roles, uno mortal y uno admin
        Rol rolMortal = new Rol(authority: "ROLE_MORTAL").save(flush: true, failOnError: true)
        Rol rolAdmin = new Rol(authority: "ROLE_ADMIN").save(flush: true, failOnError: true)

        // Asociando usaurio con un rol
        new UserRol(user: admin, rol: rolAdmin).save(flush: true, failOnError: true)
        new UserRol(user:mortal, rol:rolMortal).save(flush: true, failOnError: true)

        // Definición de la url a la que van a tener acceso los roles.
        new Requestmap(
                configAttribute: 'ROLE_ADMIN', // Significa el rol
                url: '/user/**'
        ).save(flush: true, failOnError: true)

        new Requestmap(
                configAttribute: 'ROLE_ADMIN',
                url: '/rol/**'
        ).save(flush: true, failOnError: true)


        new Requestmap(
                configAttribute: 'ROLE_ADMIN',
                url: '/requestmap/**'
        ).save(flush: true, failOnError: true)
    }
    def destroy = {
    }
}
