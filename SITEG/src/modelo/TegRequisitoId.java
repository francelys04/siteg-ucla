package modelo;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class TegRequisitoId implements Serializable {
	private Teg teg;
	private Requisito requisito;	
}
