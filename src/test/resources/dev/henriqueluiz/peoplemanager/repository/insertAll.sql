INSERT
INTO
  person
  (person_id, first_name, last_name, date_of_birth)
VALUES
  (1, 'Henrique', 'Luiz', '1999-06-14');

INSERT
INTO
  address
  (address_id, street, postal_code, number, city, preferred, person_id)
VALUES
  (1, 'Av. Universal', '24677188', '246A', 'Salvador', FALSE, 1);

INSERT
INTO
  address
  (address_id, street, postal_code, number, city, preferred, person_id)
VALUES
  (2, 'Av. Hebert', '24677136', '246A', 'Rio de Janeiro', TRUE, 1);

