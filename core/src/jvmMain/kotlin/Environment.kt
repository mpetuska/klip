package dev.petuska.klip

actual object Environment : Map<String, String> by System.getenv()