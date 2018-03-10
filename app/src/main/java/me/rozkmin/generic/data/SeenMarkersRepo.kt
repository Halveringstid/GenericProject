package me.rozkmin.generic.data

import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import javax.inject.Inject

/**
 * Created by jaroslawmichalik on 10.03.2018
 */
class SeenMarkersRepo @Inject constructor(realmConfiguration: RealmConfiguration) :
        RealmDao<String, RealmString>(
                realmConfig = realmConfiguration,
                toRealmMapper = ToRealmMapper(),
                toPojoMapper = FromRealmMapper(),
                realmClass = RealmString::class.java) {
    override fun query(specification: Specification): List<String> {
        return emptyList()
    }


}

class FromRealmMapper @Inject constructor() : Mapper<RealmString, String> {
    override fun map(from: RealmString): String = from.content
}

class ToRealmMapper @Inject constructor() : Mapper<String, RealmString> {
    override fun map(from: String): RealmString = RealmString(from)
}

open class RealmString(@PrimaryKey
                       var content: String = "") : RealmObject()