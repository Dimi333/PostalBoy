package org.dimi3.postalboy

import org.jetbrains.exposed.v1.core.Table

const val MAX_VARCHAR_LENGTH = 128

object Tasks : Table("tasks") {
    val id = integer("id").autoIncrement()
    val url = varchar("url", MAX_VARCHAR_LENGTH)

    override val primaryKey = PrimaryKey(id)
}