//package puc.unit
//
//import io.jsonwebtoken.Jwts
//import io.jsonwebtoken.security.Keys
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.test.context.SpringBootTest
//import puc.util.JwtUtil
//import java.nio.charset.StandardCharsets
//import java.util.*
//
//@SpringBootTest
//class JwtUtilTest {
//
//    private lateinit var jwtUtil: JwtUtil
//
//    @Value("\${jwt.secret}")
//    private lateinit var secretString: String
//
//    @Value("\${jwt.expiration}")
//    private var expirationTime: Long = 0
//
//    @BeforeEach
//    fun setUp() {
//        jwtUtil = JwtUtil()
//        jwtUtil.secretString = secretString
//        jwtUtil.expirationTime = expirationTime
//    }
//
//    @Test
//    fun test generateToken() {
//        val userId = 12345L
//        val token = jwtUtil.generateToken(userId)
//        assertNotNull(token)
//        val claims = Jwts.parserBuilder()
//            .setSigningKey(Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8)))
//            .build()
//            .parseClaimsJws(token)
//            .body
//        assertEquals(userId.toString(), claims.subject)
//    }
//
//    @Test
//    fun test validateToken() {
//        val userId = 12345L
//        val token = jwtUtil.generateToken(userId)
//        assertTrue(jwtUtil.validateToken(token))
//    }
//
//    @Test
//    fun test validateToken with expired token() {
//        val userId = 12345L
//        val expiredToken = Jwts.builder()
//            .setSubject(userId.toString())
//            .setIssuedAt(Date(System.currentTimeMillis() - 10000))
//            .setExpiration(Date(System.currentTimeMillis() - 5000))
//            .signWith(Keys.hmacShaKeyFor(secretString.toByteArray(StandardCharsets.UTF_8)))
//            .compact()
//        assertFalse(jwtUtil.validateToken(expiredToken))
//    }
//
//    @Test
//    fun test extractUserId() {
//        val userId = 12345L
//        val token = jwtUtil.generateToken(userId)
//        val extractedUserId = jwtUtil.extractUserId(token)
//        assertEquals(userId, extractedUserId)
//    }
//}