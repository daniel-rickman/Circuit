package dev.dancr.nexus.component

import dev.dancr.nexus.rank.Ranks

object Nametags : ServerComponent() {

    init {
        require(Ranks)
    }

}