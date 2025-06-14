package com.fst.Govoiturage.Controller;

import com.fst.Govoiturage.Entity.enums.ReservationStatus;
import com.fst.Govoiturage.Repository.UserRepository;
import com.fst.Govoiturage.Service.impl.ReservationService;
import com.fst.Govoiturage.Service.impl.RideService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private RideService rideService;

    @Autowired
    private UserRepository userRepository;

    // Affiche uniquement les réservations de l'utilisateur connecté
    @GetMapping("/reservations")
    public String viewReservations(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("loggedInUserId");

        // Redirige si l'utilisateur n'est pas connecté
        if (userId == null) {
            return "redirect:/loginn";
        }

        // Récupérer les réservations de l'utilisateur
        model.addAttribute("listReservations", reservationService.getReservationsByUserId(userId));
        return "reservations"; // Vue Thymeleaf pour afficher les réservations
    }

    // Sauvegarde une nouvelle réservation
    @PostMapping("/saveReservation")
    public String saveReservation(@RequestParam("rideId") Long rideId,
                                  @RequestParam("reservedPlaces") int reservedPlaces,
                                  HttpSession session) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            return "redirect:/loginn";
        }
        try {
            reservationService.saveReservation(rideId, userId, reservedPlaces);
        } catch (RuntimeException e) {
            return "redirect:/searchRides?error=" + e.getMessage();
        }

        return "redirect:/searchRides";
    }

    // Mise à jour du statut d'une réservation
    @PostMapping("/updateReservationStatus")
    public String updateReservationStatus(
            @RequestParam("id") Long id,
            @RequestParam("status") ReservationStatus status
    ) {
        reservationService.updateReservationStatus(id, status);
        return "redirect:/reservations"; // Redirection après la mise à jour
    }

    @PostMapping("/deleteReservation")
    public String deleteReservation(@RequestParam("id") Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }

}