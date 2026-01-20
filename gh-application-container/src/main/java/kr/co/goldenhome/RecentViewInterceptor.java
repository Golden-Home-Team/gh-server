package kr.co.goldenhome;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.goldenhome.auth.UserPrincipal;
import kr.co.goldenhome.service.RecentViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RecentViewInterceptor implements HandlerInterceptor {

    private final RecentViewService recentViewService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod hm)) return true;
        if (hm.getMethodAnnotation(TrackRecentView.class) == null) return true;

        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String facilityId = pathVariables != null ? pathVariables.get("facilityId") : null;

        if (facilityId == null) return true;
        handleRecentViewEntity(request, response, facilityId);

        return true;
    }

    private void handleRecentViewEntity(HttpServletRequest request, HttpServletResponse response, String facilityId) {
        Long fId = Long.parseLong(facilityId);
        Long userId = getUserId();
        if (userId == null) return;
        recentViewService.saveOrUpdate(userId, fId);
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal(Long userId)) {
            return userId;
        }
        return null;
    }

    private void handleRecentViewCookie(HttpServletRequest request, HttpServletResponse response, String facilityId) {
        Cookie[] cookies = request.getCookies();
        String cookieName = "recent_facilities";
        String recentIds = "";

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (cookieName.equals(c.getName())) {
                    recentIds = c.getValue();
                    break;
                }
            }
        }

        LinkedList<String> idList = new LinkedList<>();
        if (!recentIds.isEmpty()) idList.addAll(Arrays.asList(recentIds.split("\\|")));

        idList.remove(facilityId);
        idList.addFirst(facilityId);

        if (idList.size() > 5) idList.removeLast();

        String newValue = String.join("|", idList);
        Cookie newCookie = new Cookie(cookieName, newValue);
        newCookie.setPath("/");
        newCookie.setHttpOnly(true);
        newCookie.setMaxAge(60 * 60 * 24 * 7);

        response.addCookie(newCookie);
    }

}
