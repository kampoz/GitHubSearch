package com.example.kamil.githubsearch.model


/**
 * Created by Kamil on 29.06.2018.
 */
class UsersResponse {
    var incomplete_results: String? = null

    var items: MutableList<User>? = null

    var total_count: String? = null

    override fun toString(): String {
        return "UsersResponse [incomplete_results = $incomplete_results, items = $items, total_count = $total_count]"
    }
}