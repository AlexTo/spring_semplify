import {TextField} from "@material-ui/core";
import {Autocomplete} from "@material-ui/lab";
import React, {Fragment, useEffect, useState} from "react";
import {CircularProgress} from "@material-ui/core";
import {useLazyQuery} from "@apollo/react-hooks";
import {indexerQueries} from "../../queries/indexerQueries";
import {useDebounce} from "../../hooks";

function SubjectSuggestion({label}) {

  const [open, setOpen] = useState(false);
  const [options, setOptions] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const [load, {called, loading, data}] = useLazyQuery(indexerQueries.suggest);

  useEffect(() => {
    if (debouncedSearchTerm) {

      if (debouncedSearchTerm.length < 3) return;

      load({
        variables: {
          "query": {
            "text": debouncedSearchTerm,
            "type": "subject"
          }
        }
      })
    }
  }, [debouncedSearchTerm]);

  useEffect(() => {
    console.log(open);
    if (!open) {
      setOptions([]);
      return;
    }
    if (data && data.suggest) {
      setOptions(data.suggest.suggestions);
    }
  }, [open, data]);

  return (
    <Autocomplete
      open={open}
      onOpen={() => {
        setOpen(true);
      }}
      onClose={() => {
        setOpen(false);
      }}

      getOptionSelected={(option, value) => option.uri === value.uri}
      getOptionLabel={option => option.uri}
      options={options}
      loading={called && loading}
      renderInput={params => (
        <TextField
          {...params}
          label={label}
          variant="outlined"
          onChange={e => setSearchTerm(e.target.value)}
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <Fragment>
                {loading ? <CircularProgress color="inherit" size={20}/> : null}
                {params.InputProps.endAdornment}
              </Fragment>
            ),
          }}
        />)}/>
  )
}

export default SubjectSuggestion;
