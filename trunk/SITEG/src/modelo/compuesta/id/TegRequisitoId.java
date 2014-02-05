package modelo.compuesta.id;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import modelo.Requisito;
import modelo.Teg;

public class TegRequisitoId implements Serializable {
	private Teg teg;
	private Requisito requisito;	
}
