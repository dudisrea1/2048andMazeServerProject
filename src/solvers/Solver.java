package solvers;

import model.ClientRequest;

public interface Solver {
	/**
	 * Run Solver Algorithm on client request
	 * @param client request
	 * @return array of solver response
	 */
public Integer[] Run(ClientRequest request);
}
