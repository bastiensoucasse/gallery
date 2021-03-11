import axios from "axios";

export function callRestService(response, errors) {
    axios
        .get("images")
        .then((r) => {
            response = r.data;
        })
        .catch((e) => {
            errors.push(e);
        });
}
