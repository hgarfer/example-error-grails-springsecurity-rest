import com.holgergarcia.*

class BootStrap {

    def init = { servletContext ->

        User.findByUsername('testUser') ?: new User(username: 'testUser', password: 'testpassword').save(flush: true, failOnError: true)


        for(i in [0..10]){
            Parent.findByName("padre$i") ?: new Parent(name: "padre$i", surname: 'test').save(flush: true, failOnError: true)
        }
    }
    def destroy = {
    }
}
