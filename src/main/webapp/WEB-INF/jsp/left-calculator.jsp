<div class="right-panel">
    <form id="calculator-form">
        <input type="radio" name="operation" value="addition">
        <label for="Addition">(+) Addition</label><br>
        <div id="addition-inputs" class="operation-inputs" style="display: none;">
            <input type="number" id="add-input1" placeholder="x">+
            <input type="number" id="add-input2" placeholder="y">
        </div>
        <input type="radio" name="operation" value="division">
        <label for="Division">(/) Division</label><br>
        <div id="division-inputs" class="operation-inputs" style="display: none;">
            <input type="number" id="dividend" placeholder="x">+
            <input type="number" id="divisor" placeholder="y">
        </div>
        <input type="radio" name="operation" value="quadratic">
        <label for="Quadratic">(ax^2 + bx + c) Quadratic equation roots</label><br>
        <div id="quadratic-inputs" class="operation-inputs" style="display: none;">
            <input type="number" id="quad-a" placeholder="A">x^2+
            <input type="number" id="quad-b" placeholder="B">x+
            <input type="number" id="quad-c" placeholder="C">
        </div>
        <br>
        <input type="submit" value="Calculate">
        <input type="button" id="clear-results" value="Clear">
    </form>
    <div id="results-table">
        <table border="1" class="custom-table">
            <thead>
            <tr>
                <th class="table-id">Id.</th>
                <th class="table-opera">Operation</th>
                <th class="table-x">X</th>
            </tr>
            </thead>
            <tbody id="results-body">
            <!-- Tabela wyników -->
            </tbody>
        </table>
    </div>
</div>