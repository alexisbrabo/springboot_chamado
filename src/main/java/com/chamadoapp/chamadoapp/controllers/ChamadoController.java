package com.chamadoapp.chamadoapp.controllers;

import com.chamadoapp.chamadoapp.models.Chamado;
import com.chamadoapp.chamadoapp.models.Cliente;
import com.chamadoapp.chamadoapp.models.Funcionario;
import com.chamadoapp.chamadoapp.repository.ChamadoRepository;
import com.chamadoapp.chamadoapp.repository.ClienteRepository;
import com.chamadoapp.chamadoapp.repository.FuncionarioRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChamadoController {

    @Autowired
    ChamadoRepository cr;
    @Autowired
    FuncionarioRepository fr;
    @Autowired
    ClienteRepository clr;
    @Autowired
    Validator v;

    @GetMapping(value = "/cadastrarChamado")
    public ModelAndView form() {
        ModelAndView mv = new ModelAndView("chamado/formChamado");
        Iterable<Funcionario> funcionarios = fr.findAll();
        Iterable<Cliente> clientes = clr.findAll();
        mv.addObject("funcionarios", funcionarios);
        mv.addObject("clientes", clientes);
        return mv;
    }

    @GetMapping(value = "/cadastrarFuncionario")
    public ModelAndView formFuncionario() {
        ModelAndView mv = new ModelAndView("funcionario/formFuncionario");
        return mv;
    }

    @GetMapping(value = "/cadastrarCliente")
    public ModelAndView formCliente() {
        ModelAndView mv = new ModelAndView("cliente/formCliente");
        return mv;
    }

    @PostMapping(value = "/cadastrarChamado")
    public String form(@Valid Chamado chamado, BindingResult result, RedirectAttributes attributes,
                       @RequestParam Long funcionarioId, @RequestParam Long clienteId) {
        if (funcionarioId != null) {
            Funcionario f = fr.findById(funcionarioId);
            chamado.setFuncionario(f);
        }
        if (clienteId != null) {
            Cliente c = clr.findById(clienteId);
            chamado.setCliente(c);
        }

        DataBinder binder = new DataBinder(chamado);
        binder.setValidator(v);
        binder.validate();

        if (binder.getBindingResult().hasErrors()) {
            attributes.addFlashAttribute("mensagemErro", "Verifique os Campos!");
            return "redirect:/cadastrarChamado";
        }

        cr.save(chamado);

        attributes.addFlashAttribute("mensagemSucesso", "Chamado Adicionado Com Sucesso!!");
        return "redirect:/cadastrarChamado";
    }

    @PostMapping(value = "/cadastrarFuncionario")
    public String form(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagemErro", "Verifique os Campos!");
            return "redirect:/cadastrarChamado";
        }

        fr.save(funcionario);

        attributes.addFlashAttribute("mensagemSucesso", "Evento Adicionado Com Sucesso!!");
        return "redirect:/cadastrarFuncionario";
    }

    @PostMapping(value = "/cadastrarCliente")
    public String form(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagemErro", "Verifique os Campos!");
            return "redirect:/cadastrarCliente";
        }

        clr.save(cliente);

        attributes.addFlashAttribute("mensagemSucesso", "Evento Adicionado Com Sucesso!!");
        return "redirect:/cadastrarChamado";
    }

    @RequestMapping(value = {"/", "/chamados"})
    public ModelAndView listaChamados() {
        ModelAndView mv = new ModelAndView("index");
        Iterable<Chamado> chamados = cr.findAll();
        mv.addObject("chamados", chamados);
        return mv;
    }

    @RequestMapping("/deletarChamado")
    public String deletarChamado(long codigo) {
        Chamado chamado = cr.findById(codigo);
        cr.delete(chamado);
        return "redirect:/chamados";
    }

    @GetMapping(value = "/chamado/{codigo}")
    public ModelAndView detalhesChamado(long id) {
        Chamado chamado = cr.findById(id);
        ModelAndView mv = new ModelAndView("chamado/detalhesChamado");
        Iterable<Funcionario> funcionarios = fr.findAll();
        Iterable<Cliente> clientes = clr.findAll();
        mv.addObject("funcionarios", funcionarios);
        mv.addObject("clientes", clientes);
        mv.addObject("chamado", chamado);
        mv.addObject("idFunc", chamado.getFuncionario().getId());
        mv.addObject("idClient", chamado.getCliente().getId());
        return mv;
    }

    @GetMapping("/imprimirChamado")
    public void imprimirChamado(long codigo, HttpServletResponse response) throws JRException, IOException {

        Chamado chamado = cr.findById(codigo);
        List<Chamado> lista = new ArrayList<>();
        lista.add(chamado);

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);

        Map<String, Object> parametros = new HashMap<>();

        // Pega o arquivo .jasper localizado em resources
        InputStream jasperStream = this.getClass().getResourceAsStream("/relatorios/chamado.jasper");

        // Cria o objeto JaperReport com o Stream do arquivo jasper
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        // Passa para o JasperPrint o relatório, os parâmetros e a fonte dos dados, no caso uma conexão ao banco de dados
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, ds);

        // Configura a respota para o tipo PDF
        response.setContentType("application/pdf");
        // Define que o arquivo pode ser visualizado no navegador e também nome final do arquivo
        // para fazer download do relatório troque 'inline' por 'attachment'
        response.setHeader("Content-Disposition", "inline; filename=chamado.pdf");

        // Faz a exportação do relatório para o HttpServletResponse
        final OutputStream outStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
    }
}
