package br.com.caelum.viagens.voos.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.support.Route;

public class GrafoRotasUtils {

	public static boolean temSequenciaLogica(Set<? extends Route> rotas) {
		return getCaminhos(rotas).stream().filter(path -> path.getLength() == rotas.size()).findAny().isPresent();
	}
	
	public static <T extends Route> List<T> getRotasEmSequenciaLogica(Set<? extends Route> rotas) {
		List<T> rotasEmSequenciaLogica = new ArrayList<>();
		
		if(temSequenciaLogica(rotas)) {
			GrafoRotasUtils.getCaminhos(rotas, getOrigemInicial(rotas), getDestinoFinal(rotas)).forEach(path -> {
				path.getEdgeList().forEach(e -> rotasEmSequenciaLogica.add((T) e));
			});
		
			return rotasEmSequenciaLogica;
		}
		
		throw new RuntimeException("A lista de rotas não possui uma sequência lógica.");
	}

	public static Aeroporto getDestinoFinal(Set<? extends Route> rotas) {
		if (temSequenciaLogica(rotas)) {
			return getAllDestinos(rotas).stream().filter(a -> !getAllOrigens(rotas).contains(a))
					.collect(Collectors.toSet()).iterator().next();
		}

		throw new RuntimeException(
				"Não foi possível detectar o destino final já que as rotas não possuem sequência lógica.");
	}

	public static Aeroporto getOrigemInicial(Set<? extends Route> rotas) {
		if (temSequenciaLogica(rotas)) {
			return getAllOrigens(rotas).stream().filter(a -> !getAllDestinos(rotas).contains(a))
					.collect(Collectors.toSet()).iterator().next();
		}

		throw new RuntimeException(
				"Não foi possível detectar a origem inicial já que as rotas não possuem sequência lógica.");

	}

	private static List<GraphPath<Aeroporto, Route>> getCaminhos(Set<? extends Route> rotas) {
		AllDirectedPaths<Aeroporto, Route> caminhos = getAllCaminhosDiretos(rotas);
		return caminhos.getAllPaths(getAllOrigens(rotas), getAllPernaFinalCandidates(rotas), true, rotas.size());
	}
	
	private static List<GraphPath<Aeroporto, Route>> getCaminhos(Set<? extends Route> rotas, Aeroporto origem,
			Aeroporto destino) {
		AllDirectedPaths<Aeroporto, Route> caminhos = getAllCaminhosDiretos(rotas);
		return caminhos.getAllPaths(origem, destino, true, rotas.size());
	}

	private static AllDirectedPaths<Aeroporto, Route> getAllCaminhosDiretos(Set<? extends Route> rotas) {
		Graph<Aeroporto, Route> grafoRotas = getGrafoDeRotas(rotas);
		return new AllDirectedPaths<>(grafoRotas);
	}

	private static Graph<Aeroporto, Route> getGrafoDeRotas(Set<? extends Route> rotas) {
		Graph<Aeroporto, Route> grafoRotas = new DefaultDirectedGraph<>(Route.class);
		rotas.forEach(r -> {
			grafoRotas.addVertex(r.getOrigem());
			grafoRotas.addVertex(r.getDestino());
			grafoRotas.addEdge(r.getOrigem(), r.getDestino(), r);
		});
		return grafoRotas;
	}

	private static Set<Aeroporto> getAllOrigens(Set<? extends Route> rotas) {
		return rotas.stream().map(r -> r.getOrigem()).collect(Collectors.toSet());
	}

	private static Set<Aeroporto> getAllDestinos(Set<? extends Route> rotas) {
		return rotas.stream().map(r -> r.getDestino()).collect(Collectors.toSet());
	}

	private static Set<Aeroporto> getAllPernaFinalCandidates(Set<? extends Route> rotas) {
		return getAllDestinos(rotas).stream().filter(a -> !getAllOrigens(rotas).contains(a))
				.collect(Collectors.toSet());
	}
}
