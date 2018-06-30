package com.example.kamil.githubsearch


/**
 * Created by Kamil on 29.06.2018.
 */
class ReposResponse {
    var incomplete_results: String? = null

    var items: Array<Item>? = null

    var total_count: String? = null

    override fun toString(): String {
        return "ReposResponse [incomplete_results = $incomplete_results, items = $items, total_count = $total_count]"
    }
}