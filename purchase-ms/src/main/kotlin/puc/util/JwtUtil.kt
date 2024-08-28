package puc.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {

    private val secretString = UUID.randomUUID().toString()

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8))

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun extractUserId(token: String): Long? {
        return try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body

            (claims["userId"] as String).toLong()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}