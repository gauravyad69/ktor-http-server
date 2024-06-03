package models


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("public.test_table") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val fullName = varchar("fullName", 255)
    override val primaryKey = PrimaryKey(id)
}


data class User_Login(val id: Int, val username: String, val password: String)
data class User_Data(val fullName: String)

object UserDAO {

    fun getUserData(username: String, password: String): User_Data{
        return transaction {
            Users.selectAll().where { (Users.username eq username) and (Users.password eq password) }
                .map { User_Data(it[Users.fullName]) }
                .singleOrNull()!!
        }
    }



    fun addUser(username: String, password: String): User_Login {
        val id = transaction {
            Users.insert {
                it[Users.username] = username
                it[Users.password] = password
            } get Users.id
        }
        return User_Login(id, username, password)
    }

    fun getUser(username: String, password: String): User_Login? {
        return transaction {
            Users.selectAll().where { (Users.username eq username) and (Users.password eq password) }
                .map { User_Login(it[Users.id], it[Users.username], it[Users.password]) }
                .singleOrNull()
        }
    }



}
