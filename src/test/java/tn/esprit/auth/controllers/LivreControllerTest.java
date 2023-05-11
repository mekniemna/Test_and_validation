package tn.esprit.auth.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.When;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import tn.esprit.auth.controller.LivreController;
import tn.esprit.auth.entity.Livre;
import tn.esprit.auth.model.Response;
import tn.esprit.auth.service.LivreService;

@RunWith(MockitoJUnitRunner.class)
public class LivreControllerTest {

    @InjectMocks
    private LivreController livreController;

    @Mock
    private LivreService livreService;

    private List<Livre> livres;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        livres = new ArrayList<>();
        Livre livre1 = new Livre();
        livre1.setReference(1L);
        livre1.setTitre("Livre 1");
        livres.add(livre1);

        Livre livre2 = new Livre();
        livre2.setReference(2L);
        livre2.setTitre("Livre 2");
        livres.add(livre2);
    }

    @Test
    public void testAllBook() {
        // Mock the service method
   //     when(livreService.findAll()).thenReturn(livres);

        // Call the controller method
        List<Livre> result = livreController.allBook();

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindById() {
        Long ref = 1L;

        // Mock the service method
      //  when(livreService.findById(ref)).thenReturn(new Response<>(HttpStatus.OK, livres.get(0)));

        // Call the controller method
        Response<Livre> result = livreController.findById(ref);

        // Verify the result
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getCode());
        assertNotNull(result.getBody());
      //  assertEquals(livres.get(0), result.getBody().getData());
    }

    // Write more test cases to test the other methods of LivreController
}