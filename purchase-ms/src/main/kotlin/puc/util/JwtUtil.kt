package puc.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {

    private val logger = LoggerFactory.getLogger(JwtUtil::class.java)
    private val secretString = "&#5(2)rcWn@RVf`G<~9l2xMc_L2>R,,l48#}hH/"
//    private val secretKey: SecretKey = Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8))
//    private val expirationTime = 1000 * 60 * 60 // 1 hora

    fun validateToken(token: String): Boolean {
        return try {
            val secretKey = Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8))
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body

            if (claims.expiration.before(Date())) {
                logger.error("Token expirado")
                return false
            }

            true
        } catch (e: Exception) {
            logger.error("Erro ao validar token: {}", e.message)
            false
        }
    }

//    fun validateToken(token: String): Boolean {
//        return try {
//            val secretKey = Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8))
//            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
//            true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
//    }

    fun extractUserId(token: String): Long? {
        return try {
            val secretKey = Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8))
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body

            (claims["userId"] as String).toLong()
        } catch (e: Exception) {
            logger.error("Erro ao extrair userId: {}", e.message)
            null
        }
    }
}