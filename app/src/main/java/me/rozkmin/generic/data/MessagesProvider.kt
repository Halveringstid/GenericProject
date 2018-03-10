package me.rozkmin.generic.data

import io.reactivex.Single
import me.rozkmin.generic.Position
import me.rozkmin.generic.network.NetworkService
import javax.inject.Inject

/**
 * Created by jaroslawmichalik on 10.03.2018
 */
class MessagesProvider @Inject constructor(networkService: NetworkService, private val seenMarkersRepo: SeenMarkersRepo) : AbstractProvider<Pair<Position, Boolean>>(networkService = networkService) {
    override fun getOne(id: String): Single<Pair<Position, Boolean>> {
        return networkService.getAllMessages()
                .map {
                    it.first {
                        it.id.contentEquals(id)
                    }.data.copy(id = id) to isSeen().none { it.contentEquals(id) }
                }
    }

    override fun getBy(specification: Specification): Single<List<Pair<Position, Boolean>>> = Single.just(emptyList())

    override fun update(t: Pair<Position, Boolean>): Single<Pair<Position, Boolean>> {
        return seenMarkersRepo.add(t.first.id).let {
            Single.just(t)
        }
    }

    override fun getAll(): Single<List<Pair<Position, Boolean>>> {
        return networkService.getAllMessages()
                .map {
                    it
                            .map { it.data.copy(id = it.id) }
                            .map { position -> Pair(position, isSeen().none { position.id.contentEquals(it) }) }
                }

    }

    private fun isSeen(): List<String> = seenMarkersRepo.findAll()


}
