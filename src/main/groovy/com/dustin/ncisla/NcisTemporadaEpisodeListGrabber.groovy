package com.dustin.ncisla

import org.ccil.cowan.tagsoup.Parser

/**
 * Created by djohnson on 6/6/16.
 */
class NcisTemporadaEpisodeListGrabber {


    def temporadaPageSource

    public NcisTemporadaEpisodeListGrabber(temporadaPageSource) {
        this.temporadaPageSource = temporadaPageSource
    }


    List<String> episodePages() {
        def links = []

//        http.getHeaders().put("User-Agent", "Mozilla/5.0")

        def page = new XmlSlurper(new Parser()).parseText(temporadaPageSource)
        def anchors = page.'**'.findAll { it.name() == 'a' }

        for (def link : anchors) {
            def trimmedDisplayText = link.text().trim()

            // Links to episode pages
            if (trimmedDisplayText ==~ /\d+x\d+/) {
                def hrefUrl = link.@href.toString()
                println "Found episode: $trimmedDisplayText with href url: $hrefUrl "

                links.add(hrefUrl)
            }
        }

        return links
    }

    static void main(String[] args) {
        def run = new NcisTemporadaEpisodeListGrabber(this.getResourceAsStream('/ncis_la_temporada_01.html').text)
        println run.episodePages()
    }

}
