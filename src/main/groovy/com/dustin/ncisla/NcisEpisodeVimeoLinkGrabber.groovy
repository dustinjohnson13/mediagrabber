package com.dustin.ncisla

import org.ccil.cowan.tagsoup.Parser

/**
 * Created by djohnson on 6/6/16.
 */
class NcisEpisodeVimeoLinkGrabber {

    def pageSource

    public NcisEpisodeVimeoLinkGrabber(temporadaPageSource) {
        this.pageSource = temporadaPageSource
    }


    String vimeoLink() {

//        http.getHeaders().put("User-Agent", "Mozilla/5.0")

        def page = new XmlSlurper(new Parser()).parseText(pageSource)
        def anchors = page.'**'.findAll { it.name() == 'a' }

        for (def link : anchors) {
            def trimmedDisplayText = link.text().trim()

            // Links to episode pages
            if (trimmedDisplayText == 'Vimeo' || trimmedDisplayText == 'Openload' || link.@href.toString().startsWith('https://drive.google.com/file')) {
                def hrefUrl = link.@href.toString()

                println "Found href url: $hrefUrl"

                return hrefUrl
            }
        }

        return null
    }

    static void main(String[] args) {
        def run = new NcisEpisodeVimeoLinkGrabber(this.getResourceAsStream('/ncis_la_episode_1x02.html').text)
        println run.vimeoLink()
    }

}
