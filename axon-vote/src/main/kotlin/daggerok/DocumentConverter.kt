package daggerok

import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.Document
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

//@Component
//class DocumentConverter : Converter<Document, String> {
//
//  companion object {
//    val mappr = ObjectMapper()
//  }
//
//  override fun convert(source: Document?): String {
//    val s = source ?: return ""
//    val map = s.filter { it.key != "_id" }
//    return mappr.writeValueAsString(map)
//  }
//}
