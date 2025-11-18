INSERT INTO franchises (id, name) VALUES (1, 'Franquicia Central') ON DUPLICATE KEY UPDATE name='Franquicia Central';

INSERT INTO branches (id, name, franchise_id) VALUES
    (1, 'Sucursal Norte', 1),
    (2, 'Sucursal Sur', 1)
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO products (id, name, stock, branch_id) VALUES
    (1, 'Hamburguesa', 120, 1),
    (2, 'Papas Fritas', 80, 1),
    (3, 'Helado', 150, 2)
ON DUPLICATE KEY UPDATE name=VALUES(name), stock=VALUES(stock);