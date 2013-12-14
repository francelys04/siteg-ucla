package controlador;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;

@Controller
public abstract class CGeneral extends SelectorComposer<Component> {

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		inicializar(comp);
	}

	abstract void inicializar(Component comp);

}
