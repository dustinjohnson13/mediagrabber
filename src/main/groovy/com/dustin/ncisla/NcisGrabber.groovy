package com.dustin.ncisla

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import org.ccil.cowan.tagsoup.Parser

/**
 * Created by djohnson on 6/6/16.
 */
class NcisGrabber {

    static void main(String[] args) {

        HTTPBuilder http = new HTTPBuilder()
//        http.getHeaders().put("User-Agent", "Mozilla/5.0")

        def data

        http.get(uri: "http://ncislalatino.jimdo.com/2015/02/07/ncis-la-temporada-1-audio-latino/", contentType: ContentType.TEXT) { resp, reader ->
            def s = reader.text
            data = s
            new XmlSlurper(new Parser()).parseText(s)
        }

        def episodePages = new NcisTemporadaEpisodeListGrabber(data).episodePages()

        List<String> vimeoLinks = []
        for (def episodePage : episodePages) {

           http.get(uri: episodePage, contentType: ContentType.TEXT) { resp, reader ->
                def s = reader.text
                data = s
                new XmlSlurper(new Parser()).parseText(s)
            }

            def vimeoLink = new NcisEpisodeVimeoLinkGrabber(data).vimeoLink()
            if (vimeoLink) {
                vimeoLinks += vimeoLink
            } else {
                println "No Vimeo link found at: $episodePage"
            }
        }

        println vimeoLinks.join('\n')
    }

}
